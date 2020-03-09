package io.github.tetherlessworld.twxplore.cli.tree

import com.beust.jcommander.{Parameter, Parameters}
import io.github.tetherlessworld.twxplore.lib.cli.{Command, TwksClientFactory}
import io.github.tetherlessworld.twxplore.lib.tree.etl.CsvTransformer
import io.github.tetherlessworld.twxplore.lib.tree.etl.geo._
import io.github.tetherlessworld.twxplore.lib.tree.etl.tree.{TreeCsvTransformer, TwksTreeCsvTransformerSink}

object EtlCommand extends Command {
  val args = new Args()
  val name = "etl"
//  private val logger = Logger(getClass.getName)

  def apply(): Unit = {
    val twksClient = if (args.direct) TwksClientFactory.createDirectTwksClient() else TwksClientFactory.createRestTwksClient()

    args.dataSource = args.dataSource.toLowerCase

    if (args.dataSource == "all" || args.dataSource == "block")
      new BlockCsvTransformer(bufferSize = args.bufferSize).parseCsv("block.csv", new TwksGeometryCsvTransformerSink(twksClient))

    if (args.dataSource == "all" || args.dataSource == "borough")
      new BoroughCsvTransformer(bufferSize = args.bufferSize).parseCsv("borough.csv", new TwksGeometryCsvTransformerSink(twksClient))

    if (args.dataSource == "all" || args.dataSource == "city")
      new CityCsvTransformer(bufferSize = args.bufferSize).parseCsv("city.csv", new TwksGeometryCsvTransformerSink(twksClient))

    if (args.dataSource == "all" || args.dataSource == "nta")
      new NtaCsvTransformer(bufferSize = args.bufferSize).parseCsv("nta.csv", new TwksGeometryCsvTransformerSink(twksClient))

    if (args.dataSource == "all" || args.dataSource == "tree")
      new TreeCsvTransformer(bufferSize = args.bufferSize).parseCsv("tree.csv", new TwksTreeCsvTransformerSink(twksClient))
  }

  @Parameters(commandDescription = "Run the extract-transform-load (ETL) pipeline")
  class Args {
    @Parameter(description = "data source e.g., borough, city, nta, block, tree or 'all'")
    var dataSource = "all"
    @Parameter(names = Array("--direct"))
    var direct: Boolean = false
    @Parameter(names = Array("--buffer-size"))
    var bufferSize: Int = CsvTransformer.BufferSizeDefault
  }

}