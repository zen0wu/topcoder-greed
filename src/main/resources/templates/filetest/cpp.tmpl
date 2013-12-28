ifstream data("${Dependencies.testcase.GeneratedFileName}");

string next_line() {
    string s;
    getline(data, s);
    return s;
}

template <typename T> void from_stream(T &t) {
    stringstream ss(next_line());
    ss >> t;
}

void from_stream(string &s) {
    s = next_line();
}

${<if HasArray}
template <typename T> void from_stream(vector<T> &ts) {
    int len;
    from_stream(len);
    ts.clear();
    for (int i = 0; i < len; ++i) {
        T t;
        from_stream(t);
        ts.push_back(t);
    }
}
${<end}

template <typename T>
string to_string(T t) {
    stringstream s;
    s << t;
    return s.str();
}

string to_string(string t) {
    return "\\"" + t + "\\"";
}

${<if ReturnsArray}
template <typename T> string to_string(vector<T> ts) {
    stringstream s;
    s << "[ ";
    for (int i = 0; i < ts.size(); ++i) {
        if (i > 0) s << ", ";
        s << to_string(ts[i]);
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

bool do_test(${Method.Params}, ${Method.ReturnType} __expected) {
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
        cout << "           Expected: " << to_string(__expected) << endl;
        cout << "           Received: " << to_string(__result) << endl;
        return false;
    }
}

int run_test(bool mainProcess, const set<int> &case_set, const string command) {
    int cases = 0, passed = 0;
    while (true) {
        if (next_line().find("--") != 0)
            break;
${<foreach Method.Params param}
${<if !param.Type.Array}
        ${param.Type.Primitive} ${param.Name};
${<else}
        vector<${param.Type.Primitive}> ${param.Name};
${<end}
        from_stream(${param.Name});
${<end}
        next_line();
${<if !Method.ReturnType.Array}
        ${Method.ReturnType.Primitive} __answer;
${<else}
        vector<${Method.ReturnType.Primitive}> __answer;
${<end}
        from_stream(__answer);

        cases++;
${<if Options.runMultipleProcesses } 
        if (mainProcess) {
            ostringstream st; st << command << " - " << (cases-1);
            int exitCode = system(st.str().c_str());
            if (exitCode == 0) {
                passed++;
            }
            continue;
        }
${<end}
        if (case_set.size() > 0 && case_set.find(cases - 1) == case_set.end())
            continue;

        cout << "  Testcase #" << cases - 1 << " ... ";
        if ( ${if Options.runMultipleProcesses }!${end}do_test(${foreach Method.Params param , }${param.Name}${end}, __answer)) {
            ${if Options.runMultipleProcesses }return 1${else}passed++${end};
        }
    }
    if (mainProcess) {
        cout << endl << "Passed : " << passed << "/" << cases << " cases" << endl;
        int T = time(NULL) - ${CreateTime};
        double PT = T / 60.0, TT = 75.0;
        cout << "Time   : " << T / 60 << " minutes " << T % 60 << " secs" << endl;
        cout << "Score  : " << ${Problem.Score} * (0.3 + (0.7 * TT * TT) / (10.0 * PT * PT + TT * TT)) << " points" << endl;
    }
    return 0;
}

int main(int argc, char *argv[]) {
    cout.setf(ios::fixed, ios::floatfield);
    cout.precision(2);
    set<int> cases;
    bool mainProcess = true;
    for (int i = 1; i < argc; ++i) {
        if ( string(argv[i]) == "-") {
            mainProcess = false;
        } else {
            cases.insert(atoi(argv[i]));
        }
    }
    if (mainProcess) {
        cout << "${Problem.Name} (${Problem.Score} Points)" << endl << endl;
    }
    return run_test(mainProcess, cases, argv[0]);
}
