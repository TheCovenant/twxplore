import {FeatureAttributeName} from "../../states/map/FeatureAttributeName";
import {KeplerFilterType} from "../../states/map/KeplerFilterType";
import {NumericFeatureAttribute} from "./NumericFeatureAttribute";

export class FrequencyFeatureAttribute extends NumericFeatureAttribute {
  static readonly instance = new FrequencyFeatureAttribute();
  readonly name = FeatureAttributeName.frequency;
  readonly keplerFilterType = KeplerFilterType.RANGE;
}