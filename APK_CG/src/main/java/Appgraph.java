/**
 * @Author: Qinjiawei
 * @Description
 * @Date: Created in 下午5:20  18-3-1
 */
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.regex.Pattern;
//import org.xmlpull.v1.XmlPullParserException;
import java.lang.System;
import soot.PackManager;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
import soot.jimple.infoflow.source.data.SourceSinkDefinition;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;

public class Appgraph {
    public static void main(String[] args) {
        String appToRun = args[0];
        String androidPlatform = args[1];
        File app_file = new File(appToRun);
        if (app_file.isDirectory()){
            for (File file : app_file.listFiles())
            {
                System.out.println(file.getAbsolutePath());
                run(file.getAbsolutePath(), androidPlatform);
            }
        }
        else {
            System.out.println(appToRun);
            run(appToRun, androidPlatform);
        }

    }

    public static void run(String appToRun, String androidPlatform)
    {
        String filename = appToRun + ".txt";
        List<String> toInclude = Arrays.asList("java.", "android.", "org.", "com.", "javax.");
        List<String> toExclude = Arrays.asList("soot.");
        soot.G.reset();
        SetupApplication app = new SetupApplication(androidPlatform, appToRun);
        app.getConfig().setEnableStaticFieldTracking(false); //no static field tracking --nostatic
        app.getConfig().setAccessPathLength(1); // specify access path length
        app.getConfig().setFlowSensitiveAliasing(false); // alias flowin
        try {
            app.calculateSourcesSinksEntrypoints("SourcesAndSinks.txt");
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println(e.getMessage());
        } catch (Exception e) {
//            e.printStackTrace();
            System.err.println(e.getMessage());
        }

        PackManager.v().getPack("cg");
        PackManager.v().getPack("jb");
        PackManager.v().getPack("wjap.cgg");
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_process_dir(Collections.singletonList(appToRun));
        Options.v().set_android_jars(androidPlatform);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_app(true);
        Options.v().set_include(toInclude);
        Options.v().set_exclude(toExclude);
        Options.v().set_output_format(Options.output_format_xml);
        Options.v().set_output_dir("output/");
        Options.v().set_soot_classpath("lib/soot-trunk.jar:lib/soot-infoflow.jar:lib/soot-infoflow-android.jar:lib/axml-2.0.jar:lib/slf4j-simple-1.7.5.jar:lib/slf4j-api-1.7.5.jar");
        Options.v().setPhaseOption("cg", "safe-newinstance:true");
        Options.v().setPhaseOption("cg.spark", "on");
        Options.v().setPhaseOption("wjap.cgg", "show-lib-meths:true");
        Options.v().setPhaseOption("jb", "use-original-names:true");
        Scene.v().loadNecessaryClasses();
        SootMethod entryPoint = app.getEntryPointCreator().createDummyMain();
        Options.v().set_main_class(entryPoint.getSignature());
        Scene.v().setEntryPoints(Collections.singletonList(entryPoint));
        System.out.println(entryPoint.getActiveBody());
        try {
            PackManager.v().runPacks();
        }
        catch (Exception f){
            f.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(
                        new File(filename)))
        ){
            System.out.println("*************start write the file=" + filename);
            writer.write(Scene.v().getCallGraph().toString());
            writer.close();
            System.out.println("************end of write**************");
        }
        catch (IOException e){
            System.out.println("An error occurred");
        }
    }
}

