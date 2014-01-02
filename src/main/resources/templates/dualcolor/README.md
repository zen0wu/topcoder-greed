Dual color templates
====================
These are special c++ and python templates that have a different feature set than the default Greed templates. I severely customized the behavior.
* Output is a bit more verbose. For better or worse.
* Use ANSI control codes to decorate output with color.
* Most of the testing code is put in a separate file. In fact, the separate file is always the same. With some tweaks you can make it so you always use the same file. But out of the box, we use a template to generate the file in the wanted location.
* c++11 required for c++ template.
* Time out check. If a single test case needs more time than CASE_TIMEOUT, the test case will show "T" and solution will not be considered as passing.
* Multiple threads for the c++ tester allows it to detect runtime errors. Python can detect errors using exception handling.

Output
======
The output generally looks like this:

![image preview](https://dl.dropboxusercontent.com/u/95690732/greed/dualcolor.png)

Test case format
================
You will find the test cases below the code.
* The disabledTest function takes a integer i. If the function returns true, the test case with index i will be disabled. When a test case is disabled, it is not run. The score will not show as correct until all test cases are enabled and pass.
* In c++ test cases follow the format: "{ {comma-separated input arguments}, {outout} }," If the output is empty {}, the result of the test case is unknown. Which means that the test case will show as correct regardless of result (assuming it runs in time).
* In python, they are "( [comma-separated input arguments], output )," If the output is None, then the result is unknown.

Setup
=====
To use the templates included, just change your greed configuration to use the following templates: dualcolor-test (Instead of test/filetest) and dualcolor-tester. For example:
<pre>
cpp.templates = [ dualcolor-tester, dualcolor-test, source, problem-desc ]
</pre>

You can alter the filename and other configuration of the templates. Take a look to default.conf for more info.

I recommend to eventually do some customizations to the dualcolor-test template so that it uses the same external file for the tester instead of the dualcolor-tester template. Then you'd be able to customize the tester template (change colors, format, time limit, etc).

Template Options
================
There are 3 option fields that affect this template, for example:
<pre>
greed.shared.templateDef.dualcolor-test.options {
    compactMode = COMPACT_REPORT
    localTestTimeFactor = false
    singleFile  = false
    #(c++ only)
    runMultipleProcesses = true
}
</pre>

* `runMultipleProcesses` set to `false` if you prefer the single-process version.
* `compactMode` Has three options: 
 + `COMPACT_REPORT` : (default). Verbose output for each test case, a brief one-line report at the end.
 + `FULL_REPORT`    : The final report has more detail (one line per test case).
 + `ONLY_REPORT`    : That final report is the only thing displayed. (Very compact).
* `localTestTimeFactor` : If set to false, does nothing. Else it will multiply the problem's per case time limit by this value to find the local time limit. (If your computer has much different execution speed than TopCoder, it is convenient to customize this  value, 1.5, for example, adds 50% more time before a test case is reported as TLE; 0.5 will cut the execution time in half, so if your computer somehow manages to be twice as fast as TopCoder's, this is a good value.
* `disableColors`: Default is false. Enable this if your terminal cannot display ANSI-C color codes and you'd still like to use the other features.
* `customTesterLocation`: Eventually, you'd like to use a single tester.cpp / tester.py file for all the problems. Use this if you'd like to change the location of the loaded tester source.
Example:
<pre>
cpp.options.customTesterLocation = "../tester.cpp"
python.options.customTesterLocation = "../tester.py"
# no longer need the dualcolor-tester template.
shared.templates = [dualcolor-test, source, problem-desc ]
</pre>
You can find the tester.cpp / tester.py files after generating them for the first time using greed.
* `singleFile` : Included just in case there are problems making the two files to work. This one puts all the contents of the tester.cpp/tester.py inside the source file. Also need you to do the following tweaks to `templateDef.dualcolor-tester`:
<pre>
    templateDef.dualcolor-tester.templateFile = None
    templateDef.dualcolor-tester.templateFileName = None
    templateDef.dualcolor-tester.templateFileExtension = None
</pre>
Also make sure that the `templates = [ dualcolor-tester, dualcolor-test, source, problem-desc ]` calls `dualcolor-tester` before `dualcolor-test`.

You can also customize the options for c++ or python specifically by modifying `greed.language.cpp.templateDef.dualcolor-test.options` or `greed.language.java.templateDef.dualcolor-test.options`

More info
=========
http://vexorian.blogspot.com/2013/10/c-and-python-topcoder-generic-testers.html
