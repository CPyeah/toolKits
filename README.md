# Maven plugin tools

## 使用说明

```xml

<plugin>
    <!-- 将这里替换成你自己写的插件的groupId/artifactId/version -->
    <groupId>MY_PLUGIN_GROUP_ID</groupId>
    <artifactId>MY_PLUGIN_GROUP_ID</artifactId>
    <version>MY_PLUGIN_VERSION</version>
    <configuration>
        <includes>
            <include>src/main/**/*.java</include>
            <include>src/main/**/*.kt</include>
        </includes>
    </configuration>
    <executions>
        <execution>
            <id>loc</id>
            <goals>
                <goal>loc</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```