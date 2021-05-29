# What is this doc?

I will try and keep this readme document not only about the app itself(which should be simple enough) but also present my though process and how I approach solving problems.

For dependency management and general project layout I am using lein and git. I do not expect much dependencies, but I would like to have repl running on a port for my editor to connect.

# The task

So in my understanding, we are building light parser + database.
We have some form of DSL that we need to parse, then we need to store this data in some database(or datastructure). The whole thing should operate as interactive shell, meaning we will operate in a loop, reading user input and outputing infromation. There is a state, but I am assuming the state is internal to the input loop. 

Parsing:
I think we can simply use regex.
As we are not dealing with nested asts, but single line commands. For more advanced syntax instaparse might be better fit.

The proposed DSL:

We should recognize the most common pattern:
`COMMAND ARG1 ARG2 ARG3`
for example: `C Gdansk Coppenhagen 2018-10-01 5`

Initial `COMMAND` list:
C - for creating (trip) relation between 2 cities
R - for creating (return trip) reversed relation by inverting last creation
S - for searching relations
Q - for quiting (see [#extending-task])


We have to make some assumptions on the DSL.
* We expect the parser to take positional arguments.
* Valid expression matches and returns vector without nils.
* If the line is not parsed to valid expression, we will print error message, some help and prompt again for user input.
* If the value is already in "database" I am going to treat the new input values as override/correction.


Database:
This task typically would require evaluating some graph/other database solutions depending on performance expectation.

The description of the problem doesn't indicate we are going to do it, at scale. So we will most like use map(associative array) as our main datastructure for holding state. I introduce additional
"stateful" element `log`. I think it might be useful generally for debug, but I need it for getting last created relation.

# Extending the task
I had a strong urge to extend the task:

* Quality of life proposal: Adding `S` to SHOW ALL. It should be helpful when debuging in REPL.

* Generalization of the `S`. I am planning to implement search recursively, with `depth=1`. I know its not required but it seems that the solution would be more general and thus easier to grasp and extend.

_Note: on normal conduct, I would discuss this in broader group. I understand in typical projects we should talk over adding some additional features, but here I am gonna take liberty and just add them.._



Scratchpad internal data structures:

{:Gdansk
   {:Warsaw 
       {:2018-32 30}}}


Im gonna steal this:
https://dnaeon.github.io/recursively-merging-maps-in-clojure/