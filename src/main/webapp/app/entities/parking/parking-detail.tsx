import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './parking.reducer';
import { IParking } from 'app/shared/model/parking.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IParkingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ParkingDetail = (props: IParkingDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { parkingEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="parkingApp.parking.detail.title">Parking</Translate> [<b>{parkingEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="locationName">
              <Translate contentKey="parkingApp.parking.locationName">Location Name</Translate>
            </span>
          </dt>
          <dd>{parkingEntity.locationName}</dd>
          <dt>
            <span id="total">
              <Translate contentKey="parkingApp.parking.total">Total</Translate>
            </span>
          </dt>
          <dd>{parkingEntity.total}</dd>
          <dt>
            <span id="occupiedNumber">
              <Translate contentKey="parkingApp.parking.occupiedNumber">Occupied Number</Translate>
            </span>
          </dt>
          <dd>{parkingEntity.occupiedNumber}</dd>
        </dl>
        <Button tag={Link} to="/parking" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/parking/${parkingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ parking }: IRootState) => ({
  parkingEntity: parking.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ParkingDetail);
