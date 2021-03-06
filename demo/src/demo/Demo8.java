// copyright 2018 nqzero - offered under the terms of the MIT License

package demo;

import com.nqzero.permit.Safer;
import com.nqzero.permit.Permit;
import com.nqzero.permit.Safer.Meth;
import static com.nqzero.permit.Permit.getField;
import static com.nqzero.permit.Permit.unLog;
import static com.nqzero.permit.Permit.build;
import java.io.FileDescriptor;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.net.URL;
import static com.nqzero.permit.Unsafer.uu;
import static com.nqzero.permit.Permit.setAccessible;

// duplicate the full demo since it uses java 11 classes for some of the tests (which are removed here)
public class Demo8 {
    public static void main(String[] args) throws Exception {
        int [] vals = new int[10];
        int ii = 0;
        RandomAccessFile raf = new RandomAccessFile("/etc/hosts","r");
        FileDescriptor fd = raf.getFD();
        Field field = FileDescriptor.class.getDeclaredField("fd");
        unLog();
        try {
            field.setAccessible(true);
            vals[ii++] = field.getInt(fd);
        }
        catch (Throwable ex) {
            vals[ii++] = -1;
        }
        setAccessible(field);
        vals[ii++] = field.getInt(fd);


        // for java 8 and later, use uu::getInt instead of meth
        // but want this to compile with java 6
        
        Meth<Integer> meth = new Meth() {
            public Object meth(Object arg0,long arg1) {
                return uu.getInt(arg0,arg1);
            }
        };
        
        vals[ii++] = getField(fd,meth,"fd");
        vals[ii++] = getField(raf,meth,"fd","fd");
        vals[ii++] = getField(raf,meth,"fd.fd");

        Permit<FileDescriptor,?> ref = build(FileDescriptor.class,"fd");
        Permit<RandomAccessFile,?> ref2 = build(RandomAccessFile.class,"fd").chain("fd");
        Permit<RandomAccessFile,?> ref3 = build(RandomAccessFile.class,"fd.fd");
        
        vals[ii++] = ref.getInt(fd);
        vals[ii++] = ref2.getInt(raf);
        vals[ii++] = ref3.getInt(raf);
        for (int jj=0; jj < ii; jj++)
            System.out.format("ufd %2d: %4d\n",jj,vals[jj]);

        ClassLoader cl = Safer.class.getClassLoader();
        
        Permit<ClassLoader,String> app = build(cl,"ucp")
                .chain("path")
                .chain(java.util.ArrayList.class,"elementData")
                .chain("")
                .chain(URL.class,"path")
                .target(String.class);
        String path = app.link(0).getObject(cl);
        System.out.println("path: " + path);
        Permit.godMode();



    }
    
}
