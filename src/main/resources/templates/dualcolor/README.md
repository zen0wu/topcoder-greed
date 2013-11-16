Dual color templates
====================
These are special c++ and python templates that have a different feature set than the default Greed templates. I severely customized the behavior.
* Output is a bit more verbose. For better or worse.
* Use ANSI control codes to decorate output with color.
* Most of the testing code is put in a separate file. In fact, the separate file is always the same. With some tweaks you can make it so you always use the same file. But out of the box, we use a template to generate the file in the wanted location.
* c++11 required for c++ template.
* Time out check. If a single test case needs more time than CASE_TIMEOUT, the test case will show "T" and solution will not be considered as passing.

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
cpp.templates = [ dualcolor-test, source, dualcolor-tester, problem-desc ]
</pre>

You can alter the filename and other configuration of the templates. Take a look to default.conf for more info.

I recommend to eventually do some customizations to the dualcolor-test template so that it uses the same external file for the tester instead of the dualcolor-tester template. Then you'd be able to customize the tester template (change colors, format, time limit, etc).

More info
=========
http://vexorian.blogspot.com/2013/10/c-and-python-topcoder-generic-testers.html
