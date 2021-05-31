# What is this doc?

I will try and keep this readme document not only about the app itself(which should be simple enough) but also present my though process and how I approach solving problems.

For dependency management and general project layout I am using lein and git. I do not expect much dependencies, but I would like to have repl running on a port for my editor to connect.

# The task
So in my understanding, we are building light parser, resource manager(create/view) + database.
We have some form of DSL that we need to parse, then we need to store this data in some database(or datastructure). The whole thing should operate as interactive shell, meaning we will operate in a loop, reading user input and outputing infromation. There is some state, but I am assuming the state will be internal to the input loop. 

## Parsing:
I think we can simply use regex.
As we are not dealing with nested ASTs, but single line commands. For more advanced syntax `instaparse` might be better fit.
// update: I think there is a very obvious problem, with cities that have multiple words in its name. Such as: New York. This would mean we need quote parsing. Its not state in the task so I am not implementing it, just pointing out important problem.

## The proposed DSL:

We should recognize the most common pattern:
`COMMAND ARG1 ARG2 ARG3`
for example: `C Gdansk Coppenhagen 2018-10-01 5`.
At parser level I dont think we should see more speciality of ARGS.
So we expect to get 2 items `COMMAND` `[ARGS]` out of input expression. This way we have one function for routing between commands implementations.

Initial `COMMAND` list:
* C - for creating (trip) relation between 2 cities
* R - for creating (return trip) reversed relation by inverting last creation
_R should recreate a command to be reprocessed_
* S - for searching relations
* Q - for quiting (see [#extending-task])

We have to make some assumptions on the DSL.
* We expect the parser to take positional arguments in certain order. Optionality is achieved by skipping to provide last argument.
* If the line is not parsed to valid expression, we will print error message, some help and prompt again for user input.
* If the value is already in "database" I am going to treat it as additional resource(route) being added, rather then override of current resource.

## Database:
This task typically would require evaluating some graph/other database solutions depending on performance expectation.

The description of the problem doesn't indicate we are going to do it at scale. So we will use vector as our stateful structure.
We will need 2 vectors like that.
1. For routes storing.
2. For log storing.

Log storing is required for quality implementation of `R` command.

Note: There is inherent performance problem with choosing vector over map. Our searches will have linear complexity rather than O(1). I am hand-waving this issue due to:
* this is a small task and has no performance requests
* improvement from using map is not likely a candidate for final solution, most likely some dedicated db would be


# Extending the task
I had a strong urge to extend the task:
Quality of life proposals: 
* Treating command `S` without params, as show all. It should be helpful when debuging in REPL.
* Adding `Q` command, that will quit and print last state of routes. (Its always nice to let the user exit gracefully)

_Note: Normally, I would discuss this in broader group. I understand in typical projects we should talk over adding some additional features, but here I am going to take liberty and just add them._

# Building/running project
With leiningen installed run `lein run`.
If you don't have leiningen but have java installed, try: `java -jar routes-0.1.0-SNAPSHOT-standalone.jar`. 
_I am aware that leaving binary files in git is not the best practice. Its for easy access._

# Tests 
I tried to deliver some test - mostly on the most outer layer(Run command, check output).
To run tests: `lein test`
