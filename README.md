Greed 1.0
=========

Hi, this is _greed_, the missing topcoder arena plugin for algorithm contest.

Greed is good
-------------

* No CodeProcessor+FileEdit+blah blah, no tons of jars, just _**Greed**_
* No need to set up all the messes, just set your _**workspace**_, done
* Seamless migration between OS and enviroments, since all stuff are kept in workspace, in your line of sight
* Manage your code, in different _**folders**_, based on the contest and problem you're in
* Generate code based on a fully-customizable _**template**_
* Generate robust testing code, which BTW, is also _**customizable**_
* Multi language support, including Java and C++. Support for Java includes package declaration removing, helping you organize your code without worrying about the submission.

Quick start
-----------
1. Open __Topcoder arena__ -> __Login__ -> __Options__ -> __Add__  
![Add greed](https://github.com/shivawu/topcoder-greed/wiki/Add-Plugin.png)
__OK__! Remember to check __Default__ and __At startup__.

2. Select me, click __Configure__.  
![Configure greed](https://github.com/shivawu/topcoder-greed/wiki/Set-Workspace.png)
Fill in your workspace full path.

3. All set! Go get your rating! Let me worry about the rest crap.

Go rock with config
-------------------

_**Everything in greed is configuable.**_

I'm bundled with some default config, which should be enough for most of you. But if you're not satisfied, go set me.

Start with creating a file called `greed.conf` under your workspace root.

Things you can do with this config, 

#### greed.codeRoot

Change where I store your code, via `greed.codeRoot = ???`, this path is relative to your workspace root

#### greed.templates.pathPattern and fileNamePattern

This tells me where I generate and fetch your code, for different contest and problem.  

The default is `${Problem.Name}.cpp/java` in `${Contest.Name}/${Problem.Name}`. Take `TableSeating` in `SRM 249 DIV 1`, you can find your code in `workspace/code root/SRM 249/TableSeating.cpp`.

This is especially useful for Java developers, since Java resolves packages by folder structure. You can declare your package in your code template(described below), and do the same in `pathPattern`.  Write your code with your favorite IDE. That's beyond awesome! Package declaration will be auto removed while compiling and submitting in the arena.

#### greed.templates.(lang).tmplFile

Set your own template file for your preferred language.

Just set this to a relative path of your template file in your workspace, done! 

The default template for java, FYI, is

```
public class ${ClassName} {
  public ${Method.ReturnType} ${Method.Name}(${Method.Params}) {
    return ${Method.ReturnType;ZeroValue};
  }

// CUT begin
${<TestCode}
// CUT end
}
```

Cannot be simpler, can it?

Want to learn more?
-------------------
Go to my [wiki](https://github.com/shivawu/topcoder-greed/wiki). 

You'll learn how to config all the functionalities, like

* all available binded info in config, like `${Problem.Score}`
* setting the `begin cut` and `end cut` format
* how to write your own templates, including test templates

Bug Tracker
-----------
When you found a bug in me, or need a new feature, raise an issue [here](https://github.com/shivawu/topcoder-greed/issues). With the problem you're solving,
to better identify the problem.

I'll try to repair myself ASAP.

Or, consider helping me!

Contribute to me
----------------

Currently, I'm still immature, and under development.
Any help is helpful and fully appreciated.

A note, C# support is not available in me now. And my developers seems not familiar with C#, if you're willing to add this, I'll be grateful to you!

You can contribute to me in 2 ways:

1. Fork me, modify me, and send a pull request. Oh, you're not familiar with this style, well, you should. Read [this](https://help.github.com/articles/fork-a-repo).
2. Be a collaborators with my developers

License
-------

Copyright 2012 Shiva Wu

Licensed under _Apache License, Version 2.0_. You may obtain a copy of the license in the _LICENSE_ file, or at:

[http://www.apache.org/licenses/LICENSE-2.0]()

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
