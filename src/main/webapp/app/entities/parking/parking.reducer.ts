import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IParking, defaultValue } from 'app/shared/model/parking.model';

export const ACTION_TYPES = {
  FETCH_PARKING_LIST: 'parking/FETCH_PARKING_LIST',
  FETCH_PARKING: 'parking/FETCH_PARKING',
  CREATE_PARKING: 'parking/CREATE_PARKING',
  UPDATE_PARKING: 'parking/UPDATE_PARKING',
  DELETE_PARKING: 'parking/DELETE_PARKING',
  RESET: 'parking/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IParking>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ParkingState = Readonly<typeof initialState>;

// Reducer

export default (state: ParkingState = initialState, action): ParkingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PARKING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PARKING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PARKING):
    case REQUEST(ACTION_TYPES.UPDATE_PARKING):
    case REQUEST(ACTION_TYPES.DELETE_PARKING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PARKING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PARKING):
    case FAILURE(ACTION_TYPES.CREATE_PARKING):
    case FAILURE(ACTION_TYPES.UPDATE_PARKING):
    case FAILURE(ACTION_TYPES.DELETE_PARKING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARKING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARKING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PARKING):
    case SUCCESS(ACTION_TYPES.UPDATE_PARKING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PARKING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/parkings';

// Actions

export const getEntities: ICrudGetAllAction<IParking> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PARKING_LIST,
    payload: axios.get<IParking>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IParking> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PARKING,
    payload: axios.get<IParking>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IParking> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PARKING,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IParking> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PARKING,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IParking> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PARKING,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
