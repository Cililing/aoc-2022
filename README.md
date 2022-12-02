# AOC-2022

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

Example result:

```
Running challenge for day: 0
Parsing data time: 6.444646ms
Result 1: test passed, finished in 347.479us
Result 2: test passed finished in 2.507us
Day 0 finished in 1.010636ms.
```