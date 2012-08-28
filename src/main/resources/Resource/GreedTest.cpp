${<if UseArray}
// Array to vector converter
template <typename T> vector<T> vector_wrapper(T arr[], int n) { return vector<T>(arr, arr + n); }
#define to_vector(arr) vector_wrapper(arr, sizeof(arr) / sizeof(arr[0]))
${<end}

void run_testcase(${foreach Method.Params par , }${par.Type}${end}, ${Method.ReturnType} expected, int caseNo = -1);

void custom_test() {
    // You can add your custom test here
}

template <typename T> string pretty_print(T t) { stringstream s; typeid(T) == typeid(string) ? s << "\\"" << t << "\\"" : s << t; return s.str(); }

${<if UsePrintArray}
// Vector print
template <typename T> ostream &operator << (ostream &out, vector<T> arr) {
    out << "{ ";
    for (int i = 0; i < arr.size(); ++i) out << (i == 0 ? "" : ", ") << pretty_print(arr[i]);
    out << " }";
    return out;
}
${<end}

int nPassed = 0, nAll = 0;

void run_testcase(${Method.Params}, ${Method.ReturnType} expected, int caseNo) {
    static int nCustom = 0;
    nAll++;
    cout << (caseNo < 0 ? "    Custom " : "    Example") << " #" << (caseNo < 0 ? nCustom++ : caseNo) << " ... ";

${<if RecordRuntime}
    time_t startClock = clock();
    cout.setf(ios::fixed, ios::floatfield);
    cout.precision(2);
${<end}
    ${ClassName} instance;
    ${Method.ReturnType} result = instance.${Method.Name}(${foreach Method.Params par , }${par.Name}${end});
${<if RecordRuntime}
    double elapsed = (double)(clock() - startClock) / CLOCKS_PER_SEC;
${<end}

    if (result == expected) {
        nPassed++;
        cout << "PASSED!" ${if RecordRuntime}<< " (" << elapsed << " seconds)" ${end}<< endl;
    }
    else {
        cout << "FAILED!" ${if RecordRuntime}<< " (" << elapsed << " seconds)" ${end}<< endl;
        cout << "            Expected: " << pretty_print(expected) << endl;
        cout << "            Received: " << pretty_print(result) << endl;
    }
}

void run_example(int no) {
    switch (no) {${foreach Examples e}
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
            ${e.Output.Param.Type.Primitive} expected = ${e.Output};
${<else}
            ${e.Output.Param.Type.Primitive} expected[] = {${foreach e.Output.ValueList v ,}
                ${v}${end}
            };
${<end}
            run_testcase(${foreach e.Input in , }${if in.Param.Type.Array}to_vector(${in.Param.Name})${else}${in.Param.Name}${end}${end}, ${if e.Output.Param.Type.Array}to_vector(expected)${else}expected${end}, no);
            break;
        }
${<end}
    }
}

int main(int argc, char *argv[]) {
    cout << "${Problem.Name} (${Problem.Score} Points)" << endl << endl;
    if (argc == 1)
        for (int i = 0; i < ${NumOfExamples}; ++i) run_example(i);
    else
        for (int i = 1; i < argc; ++i) run_example(atoi(argv[i]));
    custom_test();

    cout << endl << "Passed " << nPassed << "/" << nAll << endl;
    return 0;
}
