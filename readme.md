<img src="logo.png" width="30%" alt="Polylith" id="logo">

Example on how to run [Hyperfiddle rcf](https://github.com/hyperfiddle/rcf) tests with [Polylith poly tool](https://polylith.gitbook.io/poly/).

To make RCF tests work with polylith you need to pass `"-Dhyperfiddle.rcf.generate-tests=true"` via **:jvm-opts** of **:poly** alias in `./deps.edn` .

```
:poly {:main-opts ["-m" "polylith.clj.core.poly-cli.core"]
                   ;; Run RCF tests when loading files in REPL
                   ;; https://github.com/hyperfiddle/rcf#ci
                   :jvm-opts ["-Dhyperfiddle.rcf.generate-tests=true"]
```

This will work, except for RCF tests under `src/` .
Polylith does not pass source paths to tests so to make tests unde `src/` work,
you will have to add them to your **:extra-paths** in **:test** alias.
Hopefully this issue is going to be fixed and RCF will work out of the box, just by providing the JVM option.

Add src to you test paths in each base / component (brick).
```
:test {
    ;; Adding src here makes poly tool discover RCF tests under src/
    :extra-paths [ "test" "src"]}

```


Clone this repo, change directory to it and then run `clojure -Srepro -M:poly test`

If all is well, you should see output that contains: `Testing poly-rcf.rcf.src-ns` . This means your test under `src/` was run as well.


```shell
clojure -Srepro -M:poly test
Projects to run tests from: rcf

Running tests for the rcf project using test runner: Polylith built-in clojure.test runner...
Running tests from the rcf project, including 1 brick: rcf

Testing poly-rcf.rcf.test-ns
✅.
Ran 1 tests containing 1 assertions.
0 failures, 0 errors.

Test results: 1 passes, 0 failures, 0 errors.

Testing poly-rcf.rcf.sample2-test
..
Ran 2 tests containing 3 assertions.
0 failures, 0 errors.

Test results: 3 passes, 0 failures, 0 errors.

Testing poly-rcf.rcf.sample-test
✅.
Ran 1 tests containing 1 assertions.
0 failures, 0 errors.

Test results: 1 passes, 0 failures, 0 errors.

Testing poly-rcf.rcf.src-ns
✅.
Ran 1 tests containing 1 assertions.
0 failures, 0 errors.

Test results: 1 passes, 0 failures, 0 errors.

Execution time: 2 seconds
```

NOTE: To see more details, use `clojure -Srepro -M:poly test :verbose`