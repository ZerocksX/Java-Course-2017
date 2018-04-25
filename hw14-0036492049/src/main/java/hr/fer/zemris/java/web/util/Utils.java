package hr.fer.zemris.java.web.util;

import hr.fer.zemris.java.web.model.Poll;
import hr.fer.zemris.java.web.model.PollOption;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilities class
 *
 * @author Pavao Jerebić
 */
public class Utils {

    /**
     * Method that reads all poll options from a file<br/>
     * format is:<br/>
     * optionTitle'\t'optionlink
     *
     * @param path path to poll option file
     * @return list of poll options
     */
    public synchronized static List<PollOption> getAllPollOptions(Path path) {
        List<PollOption> pollOptions = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> {
                String[] data = line.split("[\t]");
                PollOption pollOption = new PollOption();
                pollOption.setOptionTitle(data[0]);
                pollOption.setOptionLink(data[1]);
                pollOptions.add(pollOption);
            });
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return pollOptions;
    }

    /**
     * Method that reads all polls and their poll options and puts them in a map<br/>
     * format is:<br/>
     * title'\t'message'\t'relativePathToPollOptions
     *
     * @param path path to polls definition file
     * @return map of all polls and their list of poll options
     */
    public synchronized static Map<Poll, List<PollOption>> getAllPolls(Path path) {
        Map<Poll, List<PollOption>> pollMap = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> {
                String[] data = line.split("[\t]");
                Poll poll = new Poll();
                poll.setTitle(data[0]);
                poll.setMessage(data[1]);
                pollMap.put(poll, getAllPollOptions(Paths.get(path.getParent().toAbsolutePath().toString(), data[2])));
            });
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return pollMap;
    }

    /**
     * Method that forwards page to an errorPage mapped to "/WEB-INF/pages/errorMessage.jsp",<br/>
     * and sets "errorMessage" attribute to the given message
     *
     * @param req     request
     * @param resp    response
     * @param message message
     * @throws ServletException if something fails
     * @throws IOException      if I/O fails
     */
    public static void forwardToErrorPage(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        req.getRequestDispatcher("/WEB-INF/pages/errorMessage.jsp").forward(req, resp);
    }
}
