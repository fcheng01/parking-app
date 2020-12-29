import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Parking from './parking';
import ParkingDetail from './parking-detail';
import ParkingUpdate from './parking-update';
import ParkingDeleteDialog from './parking-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ParkingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ParkingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ParkingDetail} />
      <ErrorBoundaryRoute path={match.url} component={Parking} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ParkingDeleteDialog} />
  </>
);

export default Routes;
