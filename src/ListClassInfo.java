/*
This class implements a CLI tool that does run time class introspection
using reflection. It is similar to javap. See the help (-h) for more details.

MIT License.

Copyright (c) 2016 Joe Linoff

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject
to the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
// Package not needed, this is a standalone utility.
//package org.jlinoff.tools.listclassmethods;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class implements a CLI tools that does run time class introspection
 * using reflection.
 * <p>
 * It is useful for doing a high level analysis of classes that you are
 * not familiar with.
 *
 * @author Joe Linoff
 */
class ListClassInfo {
    /**
     * Print annotations.
     *
     * @param cls The class object.
     */
    private void annotations(Class cls) {
        System.out.println("");
        System.out.println("   annotations of " + cls.getCanonicalName());

        int n = 0;
        for (Annotation a : cls.getAnnotations()) {
            System.out.print("      ");
            System.out.printf("   %4d  %s", ++n, a);
            System.out.println("");
        }
    }

    /**
     * Print inheritance.
     *
     * @param cls The class object.
     */
    private void inheritance(Class cls) {
        System.out.println("");
        System.out.println("   inheritance ancestors " + cls.getCanonicalName());

        Stack<Class> stack = new Stack<>();
        stack.push(cls);

        Class<?> ancestor = cls.getSuperclass();
        while (ancestor != null) {
            stack.push(ancestor);
            ancestor = ancestor.getSuperclass();
        }

        int n = 0;
        while (!stack.empty()) {
            System.out.print("      ");
            System.out.printf("   %4d  %s", ++n, stack.pop());
            System.out.println("");
        }
    }

    /**
     * Print out the interfaces on a class.
     *
     * @param cls The class object.
     */
    private void interfaces(Class cls) {
        Class[] objects = cls.getInterfaces();
        Arrays.sort(objects, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        System.out.println("");
        System.out.println("   interfaces implemented by " + cls.getCanonicalName());
        int n = 0;
        for (Class obj : objects) {
            System.out.print("      ");
            System.out.printf("   %4d  %s", ++n, obj.getName());
            System.out.println("");
        }
    }

    /**
     * Print out the variables in a class.
     *
     * @param cls   The class object.
     * @param opts  Command line options.
     */
    private void variables(Class cls, Opts opts) {
        int ml = 0;
        int n = 0;

        // Get all of the variables.
        List<Field> objects = new ArrayList<>(Arrays.asList(cls.getDeclaredFields()));
        //filter(objects);
        if (!objects.isEmpty()) {
            if (!opts.m_public) {
                objects.removeIf(m -> Modifier.isPublic(m.getModifiers()));
            }
            if (!opts.m_private) {
                objects.removeIf(m -> Modifier.isPrivate(m.getModifiers()));
            }
            if (!opts.m_protected) {
                objects.removeIf(m -> Modifier.isProtected(m.getModifiers()));
            }
            if (!objects.isEmpty()) {
                if (opts.m_sort) {
                    objects.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
                }
                ml = Collections.max(objects, Comparator.comparing(a -> a.getName().length())).getName().length();
            }
        }

        System.out.println("");
        System.out.println("   declared variables in " + cls.getCanonicalName());
        for (Field obj : objects) {
            System.out.print("      ");
            System.out.printf("/* %4d  %-" + ml + "s */  ", ++n, obj.getName());
            System.out.println(obj);
        }
    }

    /**
     * Print out the methods on a class.
     *
     * @param cls   The class object.
     * @param opts  Command line options.
     */
    private void methods(Class cls, Opts opts) {
        int ml = 0;
        int n = 0;

        // Get all of the methods.
        List<Method> objects = new ArrayList<>(Arrays.asList(cls.getDeclaredMethods()));
        if (!objects.isEmpty()) {
            if (!opts.m_public) {
                objects.removeIf(m -> Modifier.isPublic(m.getModifiers()));
            }
            if (!opts.m_private) {
                objects.removeIf(m -> Modifier.isPrivate(m.getModifiers()));
            }
            if (!opts.m_protected) {
                objects.removeIf(m -> Modifier.isProtected(m.getModifiers()));
            }
            if (!objects.isEmpty()) {
                if (opts.m_sort) {
                    objects.sort((a, b) -> {
                        // Sort in a case insensitive manner.
                        int cmp = a.getName().compareToIgnoreCase(b.getName());
                        if (cmp == 0) {
                            // Overload methods. The name is the same, differentiate by arguments.
                            cmp = a.toString().compareToIgnoreCase(b.toString());
                        }
                        return cmp;
                    });
                }
                ml = Collections.max(objects, Comparator.comparing(a -> a.getName().length())).getName().length();
            }
        }

        System.out.println("");
        System.out.println("   declared methods in " + cls.getCanonicalName());
        for (Method obj : objects) {
            System.out.print("      ");
            System.out.printf("/* %4d  %-" + ml + "s */  ", ++n, obj.getName());
            System.out.println(obj);
        }
    }

// --Commented out by Inspection START (9/15/16, 8:14 PM):
//    /**
//     * Get the path to the JVM.
//     *
//     * @returns the path to the JVM.
//     */
//    private String getPathToJVM() {
//        String path = System.getProperties().getProperty("java.home") + File.separator + "bin" + File.separator + "java";
//        if (System.getProperty("os.name").startsWith("Win")) {
//            path += ".exe";
//        }
//        return path;
//    }
// --Commented out by Inspection STOP (9/15/16, 8:14 PM)

    /**
     * getClassPath and filter out system entries.
     */
    private String getClassPath() {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader) cl).getURLs();
        String cp = "";
        for (URL url : urls) {
            String us = url.toString().split(":")[1];

            // Crude attempt to filter out system path entries.
            if (us.contains("jdk1.8")) {
                continue;
            }
            if (cp.length() > 0) {
                cp += ":";
            }
            cp += us;
        }
        return cp;
    }

    /**
     * Show the packages visible in this runtime context.
     */
    private void showPackages() {
        Package[] ps = Package.getPackages();
        Arrays.sort(ps, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        int i = 0;
        System.out.println("");
        System.out.println("Packages");
        for (Package p : ps) {
            System.out.printf("%4d  %s\n", ++i, p.getName());
        }
    }

    /**
     * Print class information.
     *
     * @param name The class name.
     */
    private void printClassInfo(String name, Opts opts) {
        System.out.println("");
        try {
            ClassLoader.getSystemClassLoader().loadClass(name);
            Class cls = Class.forName(name);
            System.out.println(cls + " " + Modifier.toString(cls.getModifiers()));
            inheritance(cls);
            annotations(cls);
            interfaces(cls);
            variables(cls, opts);
            methods(cls, opts);
        } catch (ClassNotFoundException e) {
            System.err.println("WARNING: class not found: '" + name + "'.");
        }
        System.out.println("");
    }

    /**
     * Run the program.
     *
     * @param args The java class arguments.
     */
    private void run(String[] args) {
        Opts opts = new Opts(args);
        opts.m_cps.forEach(this::loadClassPath);
        opts.m_classes.forEach(n -> printClassInfo(n, opts));

        if (opts.m_packages) {
            System.out.println("");
            showPackages();
        }
    }

    /**
     * Load class path.
     *
     * @param urlString The URL string specified by the user. Files have file:// prefix. Must use abs paths.
     */
    private void loadClassPath(String urlString) {
        try {
            Class[] ps = new Class[]{URL.class};
            URL url = new URL(urlString);
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", ps);
            method.setAccessible(true);
            URLClassLoader cl = (URLClassLoader) ClassLoader.getSystemClassLoader();
            method.invoke(cl, url);
        } catch (NoSuchMethodException | MalformedURLException | InvocationTargetException | IllegalAccessException e) {
            System.err.println("ERROR: " + e);
            //e.printStackTrace();
        }
    }

// --Commented out by Inspection START (9/15/16, 8:14 PM):
//    /**
//     * Find where a file is available from the PATH environment variable.
//     *
//     * @param fileName The filename to search for.
//     */
//    private String which(String fileName) {
//        String[] dirs = System.getenv("PATH").split(":");
//        for (String dir : dirs) {
//            Path p = Paths.get(dir, fileName);
//            if (Files.exists(p)) {
//                return p.toAbsolutePath().toString();
//            }
//        }
//        return "";
//    }
// --Commented out by Inspection STOP (9/15/16, 8:14 PM):

    /**
     * Print the help message.
     */
    private void help() {
        //Class cls = MethodHandles.lookup().lookupClass().getEnclosingClass();
        Class cls = MethodHandles.lookup().lookupClass();
        String cn = cls.getName();
        String cp = "-cp " + getClassPath();

/*
        // If we are using the version of java available on
        // the path, then just use "java".
        String jvm = getPathToJVM();
        String java = which("java");
        Path p1 = Paths.get(jvm).toAbsolutePath();
        Path p2 = Paths.get(java).toAbsolutePath();
        if (p1.equals(p2)) {
            jvm = "java";
        }
*/
        String jvm = "java"; // always use "java" for the JVM.

        System.out.printf(m_help, jvm, cp, cn);
        System.exit(0);
    }

    /**
     * Process command line options.
     */
    class Opts {
        final ArrayList<String> m_classes = new ArrayList<>();
        final ArrayList<String> m_cps = new ArrayList<>();
        boolean m_packages = false;
        boolean m_public = false;
        boolean m_protected = false;
        boolean m_private = false;
        boolean m_sort = true;

        Opts(String[] args) {
            for (int i = 0; i < args.length; ++i) {
                String opt = args[i];
                switch (opt) {
                    case "-h":
                    case "-help": // java standard
                    case "--help":
                        help();
                        break;
                    case "-cp":
                    case "-l":
                    case "--load-class-path":
                        if (++i < args.length) {
                            m_cps.add(args[i]);
                        } else {
                            System.err.println("ERROR: missing argument for '" + opt + "'");
                            System.exit(1);
                        }
                        break;
                    case "-public":
                    case "--public":
                        m_public = true;
                        break;
                    case "-protected":
                    case "--protected":
                        m_protected = true;
                        break;
                    case "-private":
                    case "--private":
                        m_private = true;
                        break;
                    case "-p":
                    case "-packages":
                    case "--packages":
                        m_packages = true;
                        break;
                    case "--sort":
                        m_sort = true;
                        break;
                    case "--no-sort":
                        m_sort = false;
                        break;
                    case "-V":
                    case "-version": // java standard
                    case "--version":
                        Class cls = MethodHandles.lookup().lookupClass().getEnclosingClass();
                        System.out.println(cls.getName() + " v0.2.0");
                        System.exit(0);
                        break;
                    default:
                        m_classes.add(opt);
                        break;
                }
            }
            if (!m_public && !m_protected && !m_private) {
                m_public = m_protected = m_private = true;
            }
        }
    }

    /**
     * Tool help.
     * It would be quite useful to have multiline strings for this.
     */
    private final static String m_help = String.join("\n",
            "USAGE",
            "    %1$s %2$s %3$s [OPTIONS] [CLASSES]",
            "",
            "DESCRIPTION",
            "    Class to print out information about java classes using reflection that is",
            "    meant to be wrapped in a CLI tool.",
            "",
            "    It prints out the inheritance, annotations, interfaces, declared variables and ",
            "    declared methods for each class in a formatted way that is useful for getting a",
            "    general sense of what is there.",
            "",
            "    This tool is similar to javap but a bit more readable because it separates out",
            "    the variables and methods.",
            "",
            "    You can use it to inspect classes from arbitrary jar files or class directories.",
            "    All you need to do is specify the load class path (-l) and then specify the class name",
            "    in the normal way. See example 3 for details.",
            "",
            "    There are occasions when you don't know the package path for classes in a jar file.",
            "    When you find yourself in that position, simply run unzip -l on the jar file and",
            "    use the directory path to the class files as the package specification after",
            "    replacing the directory separators '/' with periods '.'.",
            "",
            "OPTIONS",
            "    -c CLASS, --class CLASS",
            "                       Print the methods for this class.",
            "                       It can be specified multiple times.",
            "",
            "    -h, --help         This help message.",
            "",
            "    -l URL, --load-class-path URL, -cp URL",
            "                       URL to directory containing class files or jar file.",
            "                       Used to load 3rd party classes for analysis.",
            "",
            "    -p, --packages     Show available packages from the current runtime context.",
            "                       Note that you will not see class paths loaded by -l",
            "                       until you load one of the classes explicitly.",
            "",
            "    --no-sort          Do not sort the fields.",
            "",
            "    --private          Print the private available methods and variables.",
            "                       If --private, --protected and --public are not specified",
            "                       all are printed.",
            "",
            "    --protected        Print the protected available methods and variables.",
            "                       If --private, --protected and --public are not specified",
            "                       all are printed.",
            "",
            "    --public           Print the public available methods and variables.",
            "                       If --private, --protected and --public are not specified",
            "                       all are printed.",
            "",
            "    -V, --version      Show program version and exit.",
            "",
            "EXAMPLES",
            "    $ # Example 1: help",
            "    $ %1$s %2$s %3$s -h",
            "",
            "    $ # Example 2: report class information from one of the system classes",
            "    $ %1$s %2$s %3$s java.lang.String java.lang.Integer java.lang.Package",
            "",
            "    $ # Example 3. report class information for a 3rd party class named org.foo.bar.Xyz",
            "    $ #            from the /tmp/foobar.jar file.",
            "    $ %1$s %2$s %3$s -l file:///tmp/foobar.jar org.foo.bar.Xyz",
            "",
            "    $ # Example 4. report class information for a 3rd party class named com.spam.Foo",
            "    $ #            from the a directory containing the class files: /tmp/com/spam.",
            "    $ %1$s %2$s %3$s -l file:///tmp/com.spam/ com.spam.Foo",
            "",
            "    $ # Example 5. report public class information for a 3rd party class named com.spam.Foo",
            "    $ #            from the a directory containing the class files: /tmp/com/spam.",
            "    $ #            Note the use of -cp instead of -l. They are synonymous.",
            "    $ %1$s %2$s %3$s -cp file:///tmp/com.spam/ com.spam.Foo --public",
            "",
            ""
    );

    /**
     * Tool entry point.
     * It simply instantiates the class and runs it.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        ListClassInfo obj = new ListClassInfo();
        obj.run(args);
    }
}
