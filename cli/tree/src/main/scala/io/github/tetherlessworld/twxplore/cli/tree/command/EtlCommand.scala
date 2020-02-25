package io.github.tetherlessworld.twxplore.cli.tree.command

import com.beust.jcommander.{Parameter, Parameters}
import com.typesafe.scalalogging.Logger
import io.github.tetherlessworld.twxplore.lib.base.TwksClientFactory
import io.github.tetherlessworld.twxplore.lib.tree.geo._
import io.github.tetherlessworld.twxplore.lib.tree.stores.TwksStore
import io.github.tetherlessworld.twxplore.lib.tree.{TreeDataCsvTransformer, TwksTreeCsvTransformerSink}

object EtlCommand extends Command {

  val args = new Args()
  val name = "etl"
  private val logger = Logger(getClass.getName)

  def apply(): Unit = {
    val twksClient = TwksClientFactory.createTwksClient()
    val twksStore = new TwksStore(twksClient)

    if (twksStore.getTrees(1, 0).isEmpty || true) {
      new TreeDataCsvTransformer().parseCsv(args.csvFilePath, new TwksTreeCsvTransformerSink(twksClient))
      new CityCsvTransformer().parseCsv("city.csv", new TwksGeometryCsvTransformerSink(twksClient))
      new BoroughCsvTransformer().parseCsv("nybb.csv", new TwksGeometryCsvTransformerSink(twksClient))
      new NtaCsvTransformer().parseCsv("test_ntadata.csv", new TwksGeometryCsvTransformerSink(twksClient))
      new BlockCsvTransformer().parseCsv("test_blockdata.csv", new TwksGeometryCsvTransformerSink(twksClient))
    }
  }

  @Parameters(commandDescription = "Run the extract-transform-load (ETL) pipeline")
  class Args {
    @Parameter(names = Array("--csv-file-path"), required = true)
    var csvFilePath: String = null
  }

}
