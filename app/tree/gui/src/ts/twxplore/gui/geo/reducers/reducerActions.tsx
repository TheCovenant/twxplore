import reducers from "./reducers"


const composedReducer = (state, action) => {
  switch (action.type) {
    case 'appendToMap': {
      state.app[action.map].set(action.uri, action.uri)
      let result = state
      return {
        ...result
        }
    }
  };
  return reducers(state, action);
 };

 export default composedReducer