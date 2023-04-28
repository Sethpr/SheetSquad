import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPowerCategory } from 'app/shared/model/power-category.model';
import { getEntities as getPowerCategories } from 'app/entities/power-category/power-category.reducer';
import { IPool } from 'app/shared/model/pool.model';
import { getEntities as getPools } from 'app/entities/pool/pool.reducer';
import { IPower } from 'app/shared/model/power.model';
import { getEntity, updateEntity, createEntity, reset } from './power.reducer';

export const PowerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const powerCategories = useAppSelector(state => state.powerCategory.entities);
  const pools = useAppSelector(state => state.pool.entities);
  const powerEntity = useAppSelector(state => state.power.entity);
  const loading = useAppSelector(state => state.power.loading);
  const updating = useAppSelector(state => state.power.updating);
  const updateSuccess = useAppSelector(state => state.power.updateSuccess);

  const handleClose = () => {
    navigate('/power');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPowerCategories({}));
    dispatch(getPools({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...powerEntity,
      ...values,
      owner: powerCategories.find(it => it.id.toString() === values.owner.toString()),
      pool: pools.find(it => it.id.toString() === values.pool.toString()),
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
          ...powerEntity,
          owner: powerEntity?.owner?.id,
          pool: powerEntity?.pool?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheetSquadApp.power.home.createOrEditLabel" data-cy="PowerCreateUpdateHeading">
            Create or edit a Power
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="power-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="power-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Cost"
                id="power-cost"
                name="cost"
                data-cy="cost"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Notes" id="power-notes" name="notes" data-cy="notes" type="text" />
              <ValidatedField id="power-owner" name="owner" data-cy="owner" label="Owner" type="select">
                <option value="" key="0" />
                {powerCategories
                  ? powerCategories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="power-pool" name="pool" data-cy="pool" label="Pool" type="select">
                <option value="" key="0" />
                {pools
                  ? pools.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/power" replace color="info">
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

export default PowerUpdate;
