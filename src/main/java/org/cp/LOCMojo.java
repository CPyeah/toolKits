package org.cp;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Mojo(name = "loc", defaultPhase = LifecyclePhase.COMPILE)
public class LOCMojo extends AbstractMojo {

    private static final String[] DEFAULT_INCLUDE_FILE_TYPE = {"*.java", "*.xml"};

    @Parameter(defaultValue = "${project.basedir}")
    private File basedir;

    @Parameter(defaultValue = "${project.build.sourceDirectory}")
    private File sourceDirectory;

    @Parameter(defaultValue = "${project.build.testSourceDirectory}")
    private File testSourceDirectory;

    @Parameter(defaultValue = "${project.build.resources}")
    private List<Resource> resources;

    @Parameter(defaultValue = "${project.build.testResources}")
    private List<Resource> testResources;

    @Parameter
    private String[] includes;

    private static final AtomicInteger count = new AtomicInteger(0);


    public void execute() {
        getLog().info(Arrays.toString(includes));
        countDir(basedir);
    }

    private void countDir(File directory) {
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return;
        }
        getLog().info(directory.toString());
        ArrayList<File> files = new ArrayList<>();
        collectFiles(files, directory);
        getLog().info("matched files size is " + files.size());
        countFiles(files);
        getLog().info("all files has " + count.get() + " lines.");
    }

    private void countFiles(ArrayList<File> files) {
        files.forEach(this::countFile);
    }

    private void countFile(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            lines = lines.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
            getLog().info(file.getName() + " has " + lines.size() + " line.");
            count.addAndGet(lines.size());
        } catch (IOException e) {
            getLog().error(e);
        }
    }

    /**
     * 递归搜索符合条件的文件
     *
     * @param files 文件列表
     * @param file  目录/文件
     */
    private void collectFiles(ArrayList<File> files, File file) {
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                Arrays.stream(subFiles).forEach(f -> this.collectFiles(files, f));
            }
        } else {
            if (isMatch(file)) {
                files.add(file);
            }
        }
    }

    public boolean isMatch(File file) {
        for (int i = 0; i < includes.length; i++) {
            String path = "glob:" + basedir + "/" + includes[i];
            PathMatcher matcher = FileSystems.getDefault()
                    .getPathMatcher(path);
            if (matcher.matches(file.toPath())) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String path = "glob:/Users/chengpeng/xiedaimala/maven-loc-plugin/src/main/**/*.kt";
        String f = "/Users/chengpeng/xiedaimala/maven-loc-plugin/src/main/kotlin/hello/KotlinHello.kt";
        PathMatcher matcher = FileSystems.getDefault()
                .getPathMatcher(path);
        System.out.println(matcher.matches(Paths.get(f)));
    }

}
