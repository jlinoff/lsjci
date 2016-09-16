# lsjci
Report java class info using introspection based on reflection like javap with different formatting.

Lsjci stands for list java class info. It is a wrapper script that runs a java class to report information out about classes.

This tool is similar to javap but has a different output format that i find more useful. For example it sorts the variable and method names and provides a simple to read prefix.

I have tested in on Mac OS X 10.11.6 using java-1.8.0_102 and on CentOS 7.2 using openjdk-1.8.0.101-3. It will not work with versions of java older than 1.8.

## Download and Use
You can use the tool immediately. Here is how you download and run it.
```bash
$ git clone https://github.com/jlinoff/lsjci.git
$ lsjci/bin/lsjci -version          # get the version number
$ lsjci/bin/lsjci -help             # print the help (from the underlying java class: ListClassInfo)
$ lsjci/bin/lsjci java.lang.String  # report information about the java.lang.String class
$ lsjci/bin/lsjci -cp /foo/bar/spam.jar foo.bar.spam.Spam  # report information about the foo.bar.spam.Spam class
```

Information about the available options for filtering or pruning can be found by specifying the -help option.

## Building
If you want to build the tool, there is a Makefile. You can also incorporate the source into your favorite IDE. I used IntelliJ and it works well.

Here is how you would build it from scratch.
```bash
$ git clone https://github.com/jlinoff/lsjci.git
$ cd lsjci
$ make clean    # remove the current build artifacts
$ make          # create the bin directory with lsjci and jsjci.jar
$ make help     # test
$ make version  # test
$ make test     # test
$ make testp    # test
```

NOTE: you must have java8 installed.

If java8 is installed but it is not the default you will have to edit the Makefile and src/lsjci.sh file to fix the references.

## Simple Example (java.lang.String)
Here is a simple example:
```bash
$ lsjci java.lang.String

class java.lang.String public final

   inheritance ancestors java.lang.String
            1  class java.lang.Object
            2  class java.lang.String

   annotations of java.lang.String

   interfaces implemented by java.lang.String
            1  java.io.Serializable
            2  java.lang.CharSequence
            3  java.lang.Comparable

   declared variables in java.lang.String
      /*    1  CASE_INSENSITIVE_ORDER */  public static final java.util.Comparator java.lang.String.CASE_INSENSITIVE_ORDER
      /*    2  hash                   */  private int java.lang.String.hash
      /*    3  serialPersistentFields */  private static final java.io.ObjectStreamField[] java.lang.String.serialPersistentFields
      /*    4  serialVersionUID       */  private static final long java.lang.String.serialVersionUID
      /*    5  value                  */  private final char[] java.lang.String.value

   declared methods in java.lang.String
      /*    1  charAt                   */  public char java.lang.String.charAt(int)
      /*    2  checkBounds              */  private static void java.lang.String.checkBounds(byte[],int,int)
      /*    3  codePointAt              */  public int java.lang.String.codePointAt(int)
      /*    4  codePointBefore          */  public int java.lang.String.codePointBefore(int)
      /*    5  codePointCount           */  public int java.lang.String.codePointCount(int,int)
      /*    6  compareTo                */  public int java.lang.String.compareTo(java.lang.Object)
      /*    7  compareTo                */  public int java.lang.String.compareTo(java.lang.String)
      /*    8  compareToIgnoreCase      */  public int java.lang.String.compareToIgnoreCase(java.lang.String)
      /*    9  concat                   */  public java.lang.String java.lang.String.concat(java.lang.String)
      /*   10  contains                 */  public boolean java.lang.String.contains(java.lang.CharSequence)
      /*   11  contentEquals            */  public boolean java.lang.String.contentEquals(java.lang.CharSequence)
      /*   12  contentEquals            */  public boolean java.lang.String.contentEquals(java.lang.StringBuffer)
      /*   13  copyValueOf              */  public static java.lang.String java.lang.String.copyValueOf(char[])
      /*   14  copyValueOf              */  public static java.lang.String java.lang.String.copyValueOf(char[],int,int)
      /*   15  endsWith                 */  public boolean java.lang.String.endsWith(java.lang.String)
      /*   16  equals                   */  public boolean java.lang.String.equals(java.lang.Object)
      /*   17  equalsIgnoreCase         */  public boolean java.lang.String.equalsIgnoreCase(java.lang.String)
      /*   18  format                   */  public static java.lang.String java.lang.String.format(java.lang.String,java.lang.Object[])
      /*   19  format                   */  public static java.lang.String java.lang.String.format(java.util.Locale,java.lang.String,java.lang.Object[])
      /*   20  getBytes                 */  public byte[] java.lang.String.getBytes()
      /*   21  getBytes                 */  public byte[] java.lang.String.getBytes(java.lang.String) throws java.io.UnsupportedEncodingException
      /*   22  getBytes                 */  public byte[] java.lang.String.getBytes(java.nio.charset.Charset)
      /*   23  getBytes                 */  public void java.lang.String.getBytes(int,int,byte[],int)
      /*   24  getChars                 */  public void java.lang.String.getChars(int,int,char[],int)
      /*   25  getChars                 */  void java.lang.String.getChars(char[],int)
      /*   26  hashCode                 */  public int java.lang.String.hashCode()
      /*   27  indexOf                  */  public int java.lang.String.indexOf(int)
      /*   28  indexOf                  */  public int java.lang.String.indexOf(int,int)
      /*   29  indexOf                  */  public int java.lang.String.indexOf(java.lang.String)
      /*   30  indexOf                  */  public int java.lang.String.indexOf(java.lang.String,int)
      /*   31  indexOf                  */  static int java.lang.String.indexOf(char[],int,int,char[],int,int,int)
      /*   32  indexOf                  */  static int java.lang.String.indexOf(char[],int,int,java.lang.String,int)
      /*   33  indexOfSupplementary     */  private int java.lang.String.indexOfSupplementary(int,int)
      /*   34  intern                   */  public native java.lang.String java.lang.String.intern()
      /*   35  isEmpty                  */  public boolean java.lang.String.isEmpty()
      /*   36  join                     */  public static java.lang.String java.lang.String.join(java.lang.CharSequence,java.lang.CharSequence[])
      /*   37  join                     */  public static java.lang.String java.lang.String.join(java.lang.CharSequence,java.lang.Iterable)
      /*   38  lastIndexOf              */  public int java.lang.String.lastIndexOf(int)
      /*   39  lastIndexOf              */  public int java.lang.String.lastIndexOf(int,int)
      /*   40  lastIndexOf              */  public int java.lang.String.lastIndexOf(java.lang.String)
      /*   41  lastIndexOf              */  public int java.lang.String.lastIndexOf(java.lang.String,int)
      /*   42  lastIndexOf              */  static int java.lang.String.lastIndexOf(char[],int,int,char[],int,int,int)
      /*   43  lastIndexOf              */  static int java.lang.String.lastIndexOf(char[],int,int,java.lang.String,int)
      /*   44  lastIndexOfSupplementary */  private int java.lang.String.lastIndexOfSupplementary(int,int)
      /*   45  length                   */  public int java.lang.String.length()
      /*   46  matches                  */  public boolean java.lang.String.matches(java.lang.String)
      /*   47  nonSyncContentEquals     */  private boolean java.lang.String.nonSyncContentEquals(java.lang.AbstractStringBuilder)
      /*   48  offsetByCodePoints       */  public int java.lang.String.offsetByCodePoints(int,int)
      /*   49  regionMatches            */  public boolean java.lang.String.regionMatches(boolean,int,java.lang.String,int,int)
      /*   50  regionMatches            */  public boolean java.lang.String.regionMatches(int,java.lang.String,int,int)
      /*   51  replace                  */  public java.lang.String java.lang.String.replace(char,char)
      /*   52  replace                  */  public java.lang.String java.lang.String.replace(java.lang.CharSequence,java.lang.CharSequence)
      /*   53  replaceAll               */  public java.lang.String java.lang.String.replaceAll(java.lang.String,java.lang.String)
      /*   54  replaceFirst             */  public java.lang.String java.lang.String.replaceFirst(java.lang.String,java.lang.String)
      /*   55  split                    */  public java.lang.String[] java.lang.String.split(java.lang.String)
      /*   56  split                    */  public java.lang.String[] java.lang.String.split(java.lang.String,int)
      /*   57  startsWith               */  public boolean java.lang.String.startsWith(java.lang.String)
      /*   58  startsWith               */  public boolean java.lang.String.startsWith(java.lang.String,int)
      /*   59  subSequence              */  public java.lang.CharSequence java.lang.String.subSequence(int,int)
      /*   60  substring                */  public java.lang.String java.lang.String.substring(int)
      /*   61  substring                */  public java.lang.String java.lang.String.substring(int,int)
      /*   62  toCharArray              */  public char[] java.lang.String.toCharArray()
      /*   63  toLowerCase              */  public java.lang.String java.lang.String.toLowerCase()
      /*   64  toLowerCase              */  public java.lang.String java.lang.String.toLowerCase(java.util.Locale)
      /*   65  toString                 */  public java.lang.String java.lang.String.toString()
      /*   66  toUpperCase              */  public java.lang.String java.lang.String.toUpperCase()
      /*   67  toUpperCase              */  public java.lang.String java.lang.String.toUpperCase(java.util.Locale)
      /*   68  trim                     */  public java.lang.String java.lang.String.trim()
      /*   69  valueOf                  */  public static java.lang.String java.lang.String.valueOf(boolean)
      /*   70  valueOf                  */  public static java.lang.String java.lang.String.valueOf(char)
      /*   71  valueOf                  */  public static java.lang.String java.lang.String.valueOf(char[])
      /*   72  valueOf                  */  public static java.lang.String java.lang.String.valueOf(char[],int,int)
      /*   73  valueOf                  */  public static java.lang.String java.lang.String.valueOf(double)
      /*   74  valueOf                  */  public static java.lang.String java.lang.String.valueOf(float)
      /*   75  valueOf                  */  public static java.lang.String java.lang.String.valueOf(int)
      /*   76  valueOf                  */  public static java.lang.String java.lang.String.valueOf(java.lang.Object)
      /*   77  valueOf                  */  public static java.lang.String java.lang.String.valueOf(long)
      ```
