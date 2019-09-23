# IO/MTL workshop

## Abstract
The objective of the workshop is to get some theoretical and practical overview of the functional approach to IO-based programming. You will learn how to program a real applications using [Scala](https://www.scala-lang.org/), [Cats](https://typelevel.org/cats/), [Cats Effect](https://typelevel.org/cats-effect/), [Cats MTL](https://typelevel.org/cats-mtl/), [Meow MTL](https://github.com/oleg-py/meow-mtl) and [others](build.sbt). Throughout the day we will switch between quick introductions of the core features and longer step-by-step exercises. This will expose you to all features and tools needed to create and maintain production applications.

## Table of Contents
`TODO`

## Prerequisites
  * Experience developing Scala applications.
  * Understanding of some fundamental FP concepts: immutability, pure functions, higher order functions, typeclasses.
  
## Running the application
Before launching the application you need to generate `csv` files. Please run [generate_movie_data.sh](generate_movie_data.sh) script first. Then run the app by executing `sbt run`.

## Workshop details
We are going to implement *"Movie: Hot Or Not?"* application which is a comparison-based rating system. The sample output of the application:

```
 > The Transporter (a) or The Missing Person (b) or ? (q to quit)
You chose The Transporter
 > The Transporter (a) or Malcolm X (b) or ? (q to quit)
a
You chose The Transporter
 > The Transporter (a) or The Jungle Book 2 (b) or ? (q to quit)
?
You chose nothing
 > The Transporter (a) or The Matrix (b) or ? (q to quit)
b
You chose The Matrix
```

## Acknowledgments
This project uses data from [IMDB 5000 Movie Dataset](https://www.kaggle.com/deepmatrix/imdb-5000-movie-dataset).
