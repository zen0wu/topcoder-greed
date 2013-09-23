${<if HasArray}
// Array to vector converter
template <typename T> vector<T> vector_wrapper(T arr[], int n) { return vector<T>(arr, arr + n); }
#define to_vector(arr) vector_wrapper(arr, sizeof(arr) / sizeof(arr[0]))
${<end}

template <typename T> string pretty_print(T t) { stringstream s; typeid(T) == typeid(string) ? s << "\\"" << t << "\\"" : s << t; return s.str(); }

${<if ReturnsArray}
// Vector print
template <typename T> ostream &operator << (ostream &out, vector<T> arr) {
    out << "{ ";
    for (int i = 0; i < arr.size(); ++i) out << (i == 0 ? "" : ", ") << pretty_print(arr[i]);
    out << " }";
    return out;
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

${<if RecordRuntime}
    time_t startClock = clock();
${<end}
    ${ClassName} *instance = new ${ClassName}();
    ${Method.ReturnType} __result = instance->${Method.Name}(${foreach Method.Params par , }${par.Name}${end});
${<if RecordRuntime}
    double elapsed = (double)(clock() - startClock) / CLOCKS_PER_SEC;
${<end}
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
        cout << "PASSED!" ${if RecordRuntime}<< " (" << elapsed << " seconds)" ${end}<< endl;
        return true;
    }
    else {
        cout << "FAILED!" ${if RecordRuntime}<< " (" << elapsed << " seconds)" ${end}<< endl;
        cout << "           Expected: " << pretty_print(__expected) << endl;
        cout << "           Received: " << pretty_print(__result) << endl;
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
            ${in.Param.Type.Primitive} ${in.Param.Name}[] = {${foreach in.ValueList v ,}
                ${v}${end}
            };
${<end}
${<end}
${<if !e.Output.Param.Type.Array}
            ${e.Output.Param.Type.Primitive} __expected = ${e.Output};
${<else}
            ${e.Output.Param.Type.Primitive} __expected[] = {${foreach e.Output.ValueList v ,}
                ${v}${end}
            };
${<end}
            return do_test(${foreach e.Input in , }${if in.Param.Type.Array}to_vector(${in.Param.Name})${else}${in.Param.Name}${end}${end}, ${if e.Output.Param.Type.Array}to_vector(__expected)${else}__expected${end}, __no);
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
    cout << "${Problem.Name} (${Problem.Score} Points)" << endl << endl;

    int nPassed = 0, nAll = 0;
    if (argc == 1)
        for (int i = 0; i < ${NumOfExamples}; ++i) nAll++, nPassed += run_testcase(i);
    else
        for (int i = 1; i < argc; ++i) nAll++, nPassed += run_testcase(atoi(argv[i]));
    cout << endl << "Passed : " << nPassed << "/" << nAll << " cases" << endl;

${<if RecordScore}
    int T = time(NULL) - ${CreateTime};
    double PT = T / 60.0, TT = 75.0;
    cout << "Time   : " << T / 60 << " minutes " << T % 60 << " secs" << endl;
    cout << "Score  : " << ${Problem.Score} * (0.3 + (0.7 * TT * TT) / (10.0 * PT * PT + TT * TT)) << " points" << endl;
${<end}
    return 0;
}
