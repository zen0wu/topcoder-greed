${<if !Options.cpp11}
${<if HasArray}
// Array to vector converter
template <typename T> vector<T> vector_wrapper(T arr[], int n) { return vector<T>(arr, arr + n); }
#define to_vector(arr) vector_wrapper(arr, sizeof(arr) / sizeof(arr[0]))
${<end}
${<end}

template <typename T> string to_str(T t) { stringstream s; typeid(T) == typeid(string) ? s << "\\"" << t << "\\"" : s << t; return s.str(); }

${<if ReturnsArray}
// Vector print
template <typename T> string to_str(vector<T> t) {
    stringstream s;
    s << "[ ";
    for (int i = 0; i < t.size(); ++i) {
        if (i > 0) s << ", ";
        s << to_str(t[i]);
    }
    s << " ]";
    return s.str();
}
${<end}

${<if Method.ReturnType.RealNumber}
bool double_equal(const double &a, const double &b) { return b==b && a==a && fabs(b - a) <= 1e-9 * max(1.0, fabs(a) ); }

${<if Method.ReturnType.Array}
bool double_vector_equal (const vector<double> &expected, const vector<double> &received) {
    if (expected.size() != received.size()) return false;
    int n = expected.size();
    for (int i = 0; i < n; ++i)
        if (!double_equal(expected[i], received[i])) return false;
    return true;
}
${<end}
${<end}

bool do_test(${Method.Params}, ${Method.ReturnType} __expected, int caseNo) {
    cout << "  Testcase #" << caseNo << " ... ";

    time_t startClock = clock();
    ${ClassName} *instance = new ${ClassName}();
    ${Method.ReturnType} __result = instance->${Method.Name}(${foreach Method.Params par , }${par.Name}${end});
    double elapsed = (double)(clock() - startClock) / CLOCKS_PER_SEC;
    delete instance;

${<if Method.ReturnType.RealNumber}
${<if Method.ReturnType.Array}
    if (double_vector_equal(__expected, __result)) {
${<else}
    if (double_equal(__expected, __result)) {
${<end}
${<else}
    if (__result == __expected) {
${<end}
        cout << "PASSED!" << " (" << elapsed << " seconds)" << endl;
        return true;
    }
    else {
        cout << "FAILED!" << " (" << elapsed << " seconds)" << endl;
        cout << "           Expected: " << to_str(__expected) << endl;
        cout << "           Received: " << to_str(__result) << endl;
        return false;
    }
}

bool run_testcase(int __no) {
    switch (__no) {
${<foreach Examples e}
        case ${e.Num}: {
${<foreach e.Input in}
${<if !in.Param.Type.Array}
            ${in.Param.Type.Primitive} ${in.Param.Name} = ${in};
${<else}
${<if !Options.cpp11}
            ${in.Param.Type.Primitive} ${in.Param.Name}[] = {${foreach in.ValueList v ,}
                ${v}${end}
            };
${<else}
            vector<${in.Param.Type.Primitive}> ${in.Param.Name} = {${foreach in.ValueList v ,}
                ${v}${end}
            };
${<end}
${<end}
${<end}
${<if !e.Output.Param.Type.Array}
            ${e.Output.Param.Type.Primitive} __expected = ${e.Output};
${<else}
${<if !Options.cpp11}
            ${e.Output.Param.Type.Primitive} __expected[] = {${foreach e.Output.ValueList v ,}
                ${v}${end}
            };
${<else}
            vector<${e.Output.Param.Type.Primitive}> __expected = {${foreach e.Output.ValueList v ,}
                ${v}${end}
            };
${<end}
${<end}
${<if !Options.cpp11}
            return do_test(${foreach e.Input in , }${if in.Param.Type.Array}to_vector(${in.Param.Name})${else}${in.Param.Name}${end}${end}, ${if e.Output.Param.Type.Array}to_vector(__expected)${else}__expected${end}, __no);
${<else}
            return do_test(${foreach e.Input in , }${in.Param.Name}${end}, __expected, __no);
${<end}
        }
${<end}

        // Your custom testcase goes here
        case ${NumOfExamples}:
            break;
        default: break;
    }
    return 0;
}

int main(int argc, char *argv[]) {
    cout.setf(ios::fixed,ios::floatfield);
    cout.precision(2);
    
    bool mainProcess = true;
    set<int> testCases;
    for (int i = 1; i < argc; i++) {
        if ( string(argv[i]) == "-" ) {
            mainProcess = false;
        } else {
            testCases.insert( atoi(argv[i]) );
        }
    }
    if (testCases.size() == 0) {
        for (int i = 0; i < ${NumOfExamples}; i++) {
            testCases.insert(i);
        }
    }
    if (mainProcess) {
        cout << "${Problem.Name} (${Problem.Score} Points)" << endl << endl;
    }

    int nPassed = 0;
    
    for (int i = 0; i < ${NumOfExamples}; ++i) {
        if ( testCases.empty()  || testCases.count(i) ) {
            //run
${<if Options.runMultipleProcesses }
            if (mainProcess) {
                ostringstream st; st << argv[0] << " - " << i;
                int exitCode = system(st.str().c_str());
                if (exitCode == 0) {
                    nPassed++;
                }
            } else {
                return run_testcase(i) ? 0 : 1;
            }
${<else}
            nPassed += run_testcase(i);
${<end}
        }
    }
    if (mainProcess) {
        cout << endl << "Passed : " << nPassed << "/" << testCases.size() << " cases" << endl;
        int T = time(NULL) - ${CreateTime};
        double PT = T / 60.0, TT = 75.0;
        cout << "Time   : " << T / 60 << " minutes " << T % 60 << " secs" << endl;
        cout << "Score  : " << ${Problem.Score} * (0.3 + (0.7 * TT * TT) / (10.0 * PT * PT + TT * TT)) << " points" << endl;
    }
    return 0;
}
