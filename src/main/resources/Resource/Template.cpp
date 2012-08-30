#include <cstdio>
#include <cstring>
#include <iostream>
#include <algorithm>
#include <set>
#include <vector>
#include <sstream>

using namespace std;

class ${ClassName} {
    public:
    ${Method.ReturnType} ${Method.Name}(${Method.Params}) {
        return ${Method.ReturnType;ZeroValue};
    }
};

${CutBegin}
${<TestCode}
${CutEnd}
