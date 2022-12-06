# AOC-2022

## Running

Just run: `./gradlew run`

Run with benchmark enabled: `ENABLE_BENCHMARK=true ./gradlew run`

## Writing challenges

The main function tries to find all classes annotated with `Challange`.
Then, basing on some other annotations the runner triggers proper methods.

Why so? Just wanted to make this a bit complicated. =) The second reason was that I wanted to create a framework (xD)
for running those. So I had to change the flow of control somehow 🤔

### Requirements

- test input file must follow: `input/dayX_test.txt`
- input file must follow: `input/dayX.txt`
- class must be annotated with `@Challange(day = X)`. Basing on the day the runner will get input files.
- there must be a single function annotated `@Mapper`. For
  example : `@Mapper fun parse(input: List<String>): List<InputType>`
- to run a task against input files marked the task with `@Task(id = task_id)`. The method signature must
  follow: `@Task(id = "any") fun anyName(anyName: List<InputType>): AnyType` (input type must be a returned type
  of `@Mapper`)
- all methods/constructors must be public

## Challenge structure

Each challenge must follow the structure:

```kotlin
@Challenge(0)
class Day0 {

    @Mapper
    fun parse(input: List<String>): List<Set<Char>> {
        return input.map { it.toSet() }
    }

    @Task("ex1")
    fun ex1(input: List<Set<Char>>): String {
        return input.toString()
    }
}
```

The class may be annotated with `@Benchmark(n: Int)` as well. Then the app will execute the Challenge n-times to get
some time stats. To run the benchmark you need to set a proper flag in the env (`ENABLE_BENCHMARK`).
The benchmark mode checks also if the result is deterministic. If not, it prints a warning to the console.

Example result:

```
------------------------------------------------
Start run for challenges.day0.Day0
Challenge data: @runner.Challenge(day=0)
Test data parsed in [TEST DATA NOT PRESENT]
Input data parsed in 10.526338ms
Task ex1 results:
    Test result:    [kotlin.Unit, 0s]
    Test stats:     [Stats(max=0s, min=0s, avg=0s, percentile={0.9=0s, 0.95=0s, 0.99=0s}, stdDev=0s, median=0s)]
    Run results:    [[[/,  , t, e, s]], 204.665us]
    Run stats:      [Stats(max=385.48us, min=23.849us, avg=204.665us, percentile={0.9=23.849us, 0.95=23.849us, 0.99=23.849us}, stdDev=255.712us, median=204.665us)]

------------------------------------------------
Start run for challenges.day1.Day1
Challenge data: @runner.Challenge(day=1)
Test data parsed in [TEST DATA NOT PRESENT]
Input data parsed in 10.891253ms
Task ex1 results:
    Test result:    [kotlin.Unit, 0s]
    Test stats:     [Stats(max=0s, min=0s, avg=0s, percentile={0.9=0s, 0.95=0s, 0.99=0s}, stdDev=0s, median=0s)]
    Run results:    [69836, 800.794us]
    Run stats:      [Stats(max=970.981us, min=630.606us, avg=800.794us, percentile={0.9=630.606us, 0.95=630.606us, 0.99=630.606us}, stdDev=240.681us, median=800.794us)]
Task ex2 results:
    Test result:    [kotlin.Unit, 0s]
    Test stats:     [Stats(max=0s, min=0s, avg=0s, percentile={0.9=0s, 0.95=0s, 0.99=0s}, stdDev=0s, median=0s)]
    Run results:    [207968, 10.628524ms]
    Run stats:      [Stats(max=18.627754ms, min=2.629293ms, avg=10.628524ms, percentile={0.9=2.629293ms, 0.95=2.629293ms, 0.99=2.629293ms}, stdDev=11.312620ms, median=10.628524ms)]

------------------------------------------------
Start run for challenges.day2.Day2
Challenge data: @runner.Challenge(day=2)
Test data parsed in 965.725us
Input data parsed in 5.731966ms
Task ex1 results:
    Test result:    [15, 272.019us]
    Test stats:     [Stats(max=514.44us, min=29.598us, avg=272.019us, percentile={0.9=29.598us, 0.95=29.598us, 0.99=29.598us}, stdDev=342.835us, median=272.019us)]
    Run results:    [11449, 1.023797ms]
    Run stats:      [Stats(max=1.488673ms, min=558.92us, avg=1.023797ms, percentile={0.9=558.92us, 0.95=558.92us, 0.99=558.92us}, stdDev=657.435us, median=1.023797ms)]
Task ex2 results:
    Test result:    [12, 90.911us]
    Test stats:     [Stats(max=166.262us, min=15.56us, avg=90.911us, percentile={0.9=15.56us, 0.95=15.56us, 0.99=15.56us}, stdDev=106.562us, median=90.911us)]
    Run results:    [13187, 627.341us]
    Run stats:      [Stats(max=728.138us, min=526.543us, avg=627.341us, percentile={0.9=526.543us, 0.95=526.543us, 0.99=526.543us}, stdDev=142.549us, median=627.341us)]

------------------------------------------------
Start run for challenges.day3.Day3
Challenge data: @runner.Challenge(day=3)
Test data parsed in 844.467us
Input data parsed in 1.348757ms
Task ex1 results:
    Test result:    [157, 1.075160ms]
    Test stats:     [Stats(max=2.003863ms, min=146.457us, avg=1.075160ms, percentile={0.9=146.457us, 0.95=146.457us, 0.99=146.457us}, stdDev=1.313384ms, median=1.075160ms)]
    Run results:    [7763, 10.320574ms]
    Run stats:      [Stats(max=12.802944ms, min=7.838204ms, avg=10.320574ms, percentile={0.9=7.838204ms, 0.95=7.838204ms, 0.99=7.838204ms}, stdDev=3.510601ms, median=10.320574ms)]
Task ex2 results:
    Test result:    [70, 206.665us]
    Test stats:     [Stats(max=340.056us, min=73.273us, avg=206.665us, percentile={0.9=73.273us, 0.95=73.273us, 0.99=73.273us}, stdDev=188.644us, median=206.665us)]
    Run results:    [2569, 2.587591ms]
    Run stats:      [Stats(max=2.636079ms, min=2.539103ms, avg=2.587591ms, percentile={0.9=2.539103ms, 0.95=2.539103ms, 0.99=2.539103ms}, stdDev=68.572us, median=2.587591ms)]

```

## Code Quality

1. `ktlint-format` and `ktlint-check` are present for keeping code formated
2. Qodana for static code analysis, to run:

```shell
docker run --rm -it -p 8080:8080 \
  -v `pwd`/:/data/project/ \  
  -v `pwd`/.qodana/:/data/results/ \
  jetbrains/qodana-jvm-community --show-report
```