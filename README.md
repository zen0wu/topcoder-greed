Greed
=========

Hi, this is _greed_, the missing topcoder arena plugin for algorithm contest.

Greed is good
-------------

* No CodeProcessor+FileEdit+blah blah, no tons of jars, just _**Greed**_
* No need to set up all the messes, just set your _**workspace**_, end of story
* Seamless migration between OS and enviroments, since all stuff are kept in workspace, in your line of sight
* Manage your code, in different _**folders**_, based on the contest and problem you're in
* Generate code based on a fully-customizable _**template**_
* Generate robust testing code, which BTW, is also _**customizable**_
* Multi language support, including Java, C++, and C#(thanks to @jbransen). Support for Java includes package declaration removing, helping you organize your code without worrying about the submission.

Downloads
---------

The latest version has been put into the `dist` directory in the repository, get it [here](https://github.com/shivawu/topcoder-greed/raw/master/dist/Greed-1.4.jar)!

For previous versions, go to Github's original [download page](https://github.com/shivawu/topcoder-greed/downloads).

Release Note
------------
#### 1.5
* Python support. Thanks to @wookayin, now Greed is one of the first plugins who support Python!

#### 1.4
* Major template bug fix , thanks to @ashashwat
* Template bug when allocating large space, thanks to @wookayin

#### 1.3

* unit test code generation for C#(NUnit) and Java(JUnit), thanks to @tomtung
* major rewriting

#### 1.2

* minor bug fix, mainly fix bugs on "long long" in C++, C#, and Java

#### 1.1

* update C# version, thanks to @jbransen
* major bug fix

Quick start
-----------
1. Go to [Downloads](https://github.com/shivawu/topcoder-greed/downloads), and download my latest version

2. Open __Topcoder arena__ -> __Login__ -> __Options__ -> __Editor__ -> __Add__  
![Add greed](https://github.com/shivawu/topcoder-greed/wiki/Add-Plugin.png)<br/>
__OK__! Remember to check __Default__ and __At startup__.

3. Select me, click __Configure__.  
![Configure greed](https://github.com/shivawu/topcoder-greed/wiki/Set-Workspace.png)<br/>
Fill in your workspace full path.

4. All set! Go get your rating! Let me worry about the rest crap.

Go rock with config
-------------------

_**Everything in greed is configuable.**_

I'm bundled with some default config, which should be enough for most of you. But if you're not satisfied, go set me.

Start with creating a file called `greed.conf` under your workspace root.

Things you can do with this config, 

#### greed.codeRoot

Change where I store your code, via `greed.codeRoot = ???`, this path is relative to your workspace root.
Default set to `.`, which means workspace root.

#### greed.templates.pathPattern and fileNamePattern

This tells me where I generate and fetch your code, for different contest and problem.  

The default is `${Problem.Name}.cpp/java` in `${Contest.Name}`. Take `TableSeating` in `SRM 249 DIV 1`, you can find your code in `workspace/code root/SRM 249/TableSeating.cpp`.

This is especially useful for Java developers, since Java resolves packages by folder structure.
You can declare your package in your code template(described below),
and do the same in `pathPattern`, like `src/${Contest.Name;string(lower,removespace)}`.
Write your code with your favorite IDE. That's beyond awesome!
Package declaration will be auto removed while compiling and submitting in the arena.

#### greed.templates.\<lang\>.tmplFile

For specifying your own template file for your preferred language,
Just set this to the relative path of your template file in your workspace, done! 

The default template for java, FYI, is

```
public class ${ClassName} {
  public ${Method.ReturnType} ${Method.Name}(${Method.Params}) {
    return ${Method.ReturnType;ZeroValue};
  }

${CutBegin}
${<TestCode}
${CutEnd}
}
```

Cannot be simpler, can it?

#### greed.templates.\<lang\>.testTmplFile

This is where the magic happens!

You can do __meta programming__ with your testing code template.

There're several built-in test templates.

| Resource path      | Language | Tested on | Comment |
| ------------------ | :------: | --------- | ------- |
| `res:/GCCTest.cpp` |  C++     | GCC on Mac OS X & Cygwin GCC on Windows | Each testcase run in different instance. Runtime error is not detected and terminates the whole program. Pass testcases numbers to run by command-line arguments, none means all testcases. |
| `res:/Test.java`   | Java     | JDK7 on Mac OS X | Each testcase run in different instance. Runtime exception detected. Pass testcases numbers to run by command-line arguments, none means all testcases. |
| `res:/Test.cs`     | C#       | Mono on Mac OS X | Same as Java vesrion |

If you're not satisfied with the default template, consider DIY your own!
See the [Templates](https://github.com/shivawu/topcoder-greed/wiki/Templates) page on my wiki.

And please, please, __contribute good templates to me__, including templates for different environments, and with more functionalities. Everyone will benefit from your templates. How cool is this!

#### greed.test.unitTest

If you've got a powerful IDE with great unit testing and debugging support, why mingle test code with your solution? For C# and Java, set `greed.test.unitTest` to `true`, and the unit tests will be generated in a separate file according to `greed.templates.<lang>.unitTestTmplFile`(If not set or invalid, I will fallback to normal test code generation). Then you can link both files to an existing IDE project:

![VS Add File Menu](https://raw.github.com/wiki/tomtung/topcoder-greed/VS-Add-Existing.png)

![VS Link Existing File](https://raw.github.com/wiki/tomtung/topcoder-greed/VS-Add-Existing-As-Link.png)

and you'll be able to utilize the IDE to easily test and debug each or all test cases:

![Resharper NUnit Debugging](https://raw.github.com/wiki/tomtung/topcoder-greed/Resharper-NUnit-Debug.png)

Want to learn more?
-------------------
Go to my [wiki](https://github.com/shivawu/topcoder-greed/wiki). 

You'll learn how to config all the functionalities, like

* all available binded info in config, like `${Problem.Score}`
* setting the `begin cut` and `end cut` format
* how to write your own templates, including test templates

Bug Tracker
-----------
When you found a bug in me, or need a new feature, raise an issue [here](https://github.com/shivawu/topcoder-greed/issues).
Please, with the problem you're solving and the room you're in, to better identify the problem.

I'll try to repair myself ASAP.

Or, consider helping me!

Contribute to me
----------------

Currently, I'm still immature, and under development.
Any help is helpful and greatly appreciated.

You can contribute to me in 2 ways:

1. Fork me, modify me, and send a pull request. Oh, you're not familiar with this style, well, you should. Read [this article](https://help.github.com/articles/fork-a-repo).
2. Be a collaborators with my developers

License
-------

Copyright 2012 Greed

Licensed under _Apache License, Version 2.0_. You may obtain a copy of the license in the _LICENSE_ file, or at:

[http://www.apache.org/licenses/LICENSE-2.0]()

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
