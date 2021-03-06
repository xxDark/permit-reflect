// copyright 2018 nqzero - offered under the terms of the MIT License

package demo;

import java.io.FileDescriptor;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;

public class DemoNormal {
    public static void main(String[] args) throws Exception {
        int [] vals = new int[10];
        int ii = 0;
        RandomAccessFile raf = new RandomAccessFile("/etc/hosts","r");
        FileDescriptor fd = raf.getFD();
        Field field = FileDescriptor.class.getDeclaredField("fd");
        Class log = Class.forName("jdk.internal.module.IllegalAccessLogger");
        System.out.println("logger: " + DemoPermit.logger(false));
        try {
            field.setAccessible(true);
            vals[ii++] = field.getInt(fd);
        }
        catch (Throwable ex) {
            vals[ii++] = -1;
        }
        vals[ii++] = field.getInt(fd);
        


        jdk.internal.jshell.tool.JShellToolBuilder obj = new jdk.internal.jshell.tool.JShellToolBuilder();
        jdk.internal.jshell.tool.JShellTool tool = obj.rawTool();
        if (args.length > 0)
            tool.start(args);

        System.out.println("tool: " + obj);
        
        // work around bug in jshell (dangling threads - bug has been reported and accepted by openjdk)
        System.exit(0);
    }
    
}
