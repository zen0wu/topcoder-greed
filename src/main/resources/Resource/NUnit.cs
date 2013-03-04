using NUnit.Framework;

[TestFixture]
public class ${ClassName}Test
{
${<if Method.ReturnType.RealNumber}
	[SetUp]
	public void Setup()
	{
		GlobalSettings.DefaultFloatingPointTolerance = 1e-9;
	}

${<end}
${<foreach Examples e}
	[Test]
	public void Example${e.Num}()
	{
${<foreach e.Input in}
${<if !in.Param.Type.Array}
		${in.Param.Type.Primitive} ${in.Param.Name} = ${in};
${<else}
		${in.Param.Type.Primitive}[] ${in.Param.Name} = new ${in.Param.Type} {${foreach in.ValueList v ,}
			${v}${end}
		};
${<end}
${<end}
${<if !e.Output.Param.Type.Array}
		${e.Output.Param.Type.Primitive} __expected = ${e.Output};
${<else}
		${e.Output.Param.Type.Primitive}[] __expected = new ${e.Output.Param.Type} {${foreach e.Output.ValueList v ,}
			${v}${end}
		};
${<end}
		${Method.ReturnType} __result = new ${ClassName}().${Method.Name}(${foreach e.Input in , }${in.Param.Name}${end});
		Assert.AreEqual(__result, __expected);
	}

${<end}
}
