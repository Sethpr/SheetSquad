import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPool } from 'app/shared/model/pool.model';
import { getEntity, updateEntity, createEntity, reset } from './pool.reducer';

export const PoolUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const poolEntity = useAppSelector(state => state.pool.entity);
  const loading = useAppSelector(state => state.pool.loading);
  const updating = useAppSelector(state => state.pool.updating);
  const updateSuccess = useAppSelector(state => state.pool.updateSuccess);

  const handleClose = () => {
    navigate('/pool');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...poolEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...poolEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheetSquadApp.pool.home.createOrEditLabel" data-cy="PoolCreateUpdateHeading">
            Create or edit a Pool
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="pool-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Normal" id="pool-normal" name="normal" data-cy="normal" type="text" />
              <ValidatedField label="Hard" id="pool-hard" name="hard" data-cy="hard" type="text" />
              <ValidatedField label="Wiggle" id="pool-wiggle" name="wiggle" data-cy="wiggle" type="text" />
              <ValidatedField label="Expert" id="pool-expert" name="expert" data-cy="expert" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pool" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PoolUpdate;
