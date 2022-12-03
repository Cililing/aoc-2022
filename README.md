# AOC-2022

## Running

Just run: `./gradlew run`
Run with benchmark enabled: `ENABLE_BENCHMARK=true ./gradlew run`

## Writing challenges

The main function tries to find all classes annotated with `Challange`.
Then creates them via reflection and trigger proper functions with time measurement.

Why so? Just wanted to make this a bit complicated. =) The second reason was that I wanted to create a framework (xD)
for running those. So I had to change the flow of control somehow 🤔

## Challenge structure

Each challenge must follow the structure:

```kotlin
@Challenge(day = 0) // challange day
class Day0 : BaseChallange<InputType> {

    override val inputPath = "input/day0.txt" // path with the input data

    // this method is invoked first, it maps the input (loaded as a list of string, each element is a new line in the file)
    // the method will be loaded at first, before calculation of ex1/ex2
    // to access the result (parsed input) refer to `input` variable (which is protected in the file)
    override fun parse(input: List<String>): List<InputType> {
        return input.map { InputType(input) }
    }

    override fun ex1(): String {
        return "ex1 result"
    }

    override fun ex2(): String {
        return "ex2 result"
    }
}
```

For an input "dayX.txt" the app will try to find a file "dayX_test.txt" (**file must have extension**) to run the
challenge against the test data.

The class may be annotated with `@Benchmark(n: Int)` as well. Then the app will execute the Challenge n-times to get the
average time. To run the benchmark you need to set a proper flag in the env (`enableBenchmark`).

Example result:

```
----------------------------------------
Running challenge for day: 0
Parsing data time: 4.233289ms
Result 1: test passed, finished in 632.095us
Result 2: test passed finished in 19.299us
Day 0 finished in 688.935us.
Average time (1 runs): [632.095us, 19.299us]

----------------------------------------
Running challenge for day: 1
Parsing data time: 46.801874ms
Result 1: 69836, finished in 1.096371ms
Result 2: 207968 finished in 17.919382ms
Day 1 finished in 19.046762ms.
Average time (1 runs): [1.096371ms, 17.919382ms]

----------------------------------------
Running challenge for day: 2
Result 1/Test: 15 finished in 639.377us
Result 2/Test: 12 finished in 10.447us
Parsing data time: 4.455945ms
Result 1: 11449, finished in 1.755163ms
Result 2: 13187 finished in 1.735761ms
Day 2 finished in 3.518039ms.
Average time (1 runs): [1.755163ms, 1.735761ms]
```