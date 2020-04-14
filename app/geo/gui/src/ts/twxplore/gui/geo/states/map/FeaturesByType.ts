import {MapFeature} from "./MapFeature";
import {MapFeatureTypeState} from "./MapFeatureTypeState";
import {FilterStateTypes} from "./MapFilterStateTypes";

export interface FeaturesByType {
  features: MapFeature[];
  dirty: boolean;
  featureTypeState: MapFeatureTypeState;
  filterState: {[attribute: string]: FilterStateTypes};
}
