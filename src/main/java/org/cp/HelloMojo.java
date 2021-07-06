package org.cp;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "sayHi")
public class HelloMojo extends AbstractMojo {

    @Parameter(property = "name", defaultValue = "maven plugin")
    private String name;

    public void execute() {
        getLog().info("Hello " + name + " !!");
    }
}
