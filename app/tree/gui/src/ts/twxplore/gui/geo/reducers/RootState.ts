import {RouterState} from "react-router-redux";
import {AppState} from "twxplore/gui/geo/reducers/AppState";

export interface RootState {
    keplerGl: any
    app: AppState
    routing: RouterState
}