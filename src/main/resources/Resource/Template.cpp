#include <cstdio>
#include <cmath>
#include <cstring>
#include <ctime>
#include <iostream>
#include <algorithm>
#include <set>
#include <map>
#include <vector>
#include <sstream>
#include <typeinfo>

#ifdef DEBUG
    #define debug(args...) {dbg,args;std::cout<<std::endl;}
#else
    #define debug(args...) // Just strip off all debug tokens
#endif

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
