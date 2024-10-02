//
//
//
////@main def main(): Unit = {
////  val registry = Encodings.newDefaultEncodingRegistry()
////  val encoding = registry.getEncoding(EncodingType.CL100K_BASE)
////
////  val sentences: List[String] = List(
////    "Mark Grechanik",
////    "Hello World",
////    "Today is rainy",
////    "Joint Probability distribution"
////  )
////
////  val encodedSentences = sentences.map(encoding.encode)
////  encodedSentences.foreach(encoded => println(encoded.toString))
////
////  val decodedSentences = encodedSentences.map(encoding.decode)
////  decodedSentences.foreach(println)
////}
//
//
//
//import com.knuddels.jtokkit.Encodings
//import com.knuddels.jtokkit.api.{Encoding, EncodingType, IntArrayList}
//import scala.io.Source
//import java.io.{File, PrintWriter}
//
//@main def main(): Unit = {
//  // Initialize encoding registry and get the desired encoding
//  val registry = Encodings.newDefaultEncodingRegistry()
//  val encoding = registry.getEncoding(EncodingType.CL100K_BASE)
//
//  // Specify input and output files
//  val inputFile = "input.txt"
//  val outputFile = "output.csv"
//
//  // Read sentences from input file
//  val sentences = Source.fromFile(inputFile).getLines().toList
//  Source.fromFile(inputFile).close() // Ensure the file is closed after reading
//
//  // Encode the sentences using the provided encoding
//  val encodedSentences: List[IntArrayList] = sentences.map(encoding.encode)
//
//  // Flatten the encoded tokens and calculate their frequencies
//  val tokenFrequencies = encodedSentences
//    .flatMap(list => list.toArray.map(_.toInt).toList) // Convert IntArrayList to Array, then to Scala List
//    .groupBy(identity)                                 // Group by token
//    .view.mapValues(_.size)                            // Count occurrences
//    .toMap
//
//  // Write the token frequencies to the output CSV file
//  val writer = new PrintWriter(new File(outputFile))
//  try {
//    writer.write("Token,Frequency\n")
//    tokenFrequencies.foreach { case (token, frequency) =>
//      writer.write(s"$token,$frequency\n")
//    }
//  } finally {
//    writer.close() // Ensure the writer is closed properly
//  }
//
//  println(s"Token frequencies saved to $outputFile")
//}


import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.{Encoding, EncodingType, IntArrayList}
import scala.io.Source
import java.io.{File, PrintWriter}

@main def main(): Unit = {
  // Initialize encoding registry and get the desired encoding
  val registry = Encodings.newDefaultEncodingRegistry()
  val encoding = registry.getEncoding(EncodingType.CL100K_BASE)

  // Specify input and output files
  val inputFile = "input.txt"
  val outputFile = "output.csv"

  // Read sentences from input file
  val sentences = Source.fromFile(inputFile).getLines().toList
  Source.fromFile(inputFile).close() // Ensure the file is closed after reading

  // Encode the sentences and pair each sentence with its encoded tokens
  val encodedSentencesWithWords: List[(String, IntArrayList)] = sentences.map(sentence => (sentence, encoding.encode(sentence)))

  // Flatten the encoded tokens and calculate their frequencies, but keep the word association
  val wordTokenFrequencies = encodedSentencesWithWords.flatMap { case (word, tokens) =>
    tokens.toArray.map(token => (word, token.toInt)).toList
  }

  // Group by word and token, then calculate frequencies
  val groupedFrequencies = wordTokenFrequencies
    .groupBy(identity) // Group by (word, token) pair
    .view.mapValues(_.size) // Count occurrences
    .toMap

  // Write the word-token frequencies to the output CSV file
  val writer = new PrintWriter(new File(outputFile))
  try {
    writer.write("Word,Token,Frequency\n")
    groupedFrequencies.foreach { case ((word, token), frequency) =>
      writer.write(s"$word,$token,$frequency\n")
    }
  } finally {
    writer.close() // Ensure the writer is closed properly
  }

  println(s"Word-token frequencies saved to $outputFile")
}
