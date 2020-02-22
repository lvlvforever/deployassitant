package site.daipeng.tool.deployassistant.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author daipeng
 * @date 2019/8/6 21:54
 * @description
 */
@RestController
@RequestMapping("deploy")
public class AssistantController {
    private String defaultScript = "~/start.sh";

    private ExecutorService es = Executors.newFixedThreadPool(10);

    @GetMapping("")
    public String deploy(@RequestParam(required = false, defaultValue = "") String script) throws  Exception {
        String runScript = StringUtils.isEmpty(script) ? defaultScript : script;
        final  Process process = Runtime.getRuntime().exec(runScript);

        es.submit(() -> {
            try {
                process.waitFor();

            } catch (Exception e) {
                System.err.println(e.getStackTrace());

            }
        });

        es.submit(() -> {

            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));) {

                String line = null;
                while ((line = in.readLine()) != null) {
                    System.err.println(line);
                }
            } catch (Exception e) {

            }
        });
        es.submit(() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getErrorStream()));) {

                String line = null;
                while ((line = in.readLine()) != null) {
                    System.err.println(line);
                }
            } catch (Exception e) {

            }
        });


        return "success";
    }

}
