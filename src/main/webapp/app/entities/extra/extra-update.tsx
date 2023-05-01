import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBaseExtra } from 'app/shared/model/base-extra.model';
import { getEntities as getBaseExtras } from 'app/entities/base-extra/base-extra.reducer';
import { IExtra } from 'app/shared/model/extra.model';
import { Capacity } from 'app/shared/model/enumerations/capacity.model';
import { getEntity, updateEntity, createEntity, reset } from './extra.reducer';

export const ExtraUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const baseExtras = useAppSelector(state => state.baseExtra.entities);
  const extraEntity = useAppSelector(state => state.extra.entity);
  const loading = useAppSelector(state => state.extra.loading);
  const updating = useAppSelector(state => state.extra.updating);
  const updateSuccess = useAppSelector(state => state.extra.updateSuccess);
  const capacityValues = Object.keys(Capacity);

  const handleClose = () => {
    navigate('/extra');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBaseExtras({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...extraEntity,
      ...values,
      base: baseExtras.find(it => it.id.toString() === values.base.toString()),
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
          capacity: 'SPEED',
          ...extraEntity,
          base: extraEntity?.base?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheetSquadApp.extra.home.createOrEditLabel" data-cy="ExtraCreateUpdateHeading">
            Create or edit a Extra
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="extra-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Multiplier"
                id="extra-multiplier"
                name="multiplier"
                data-cy="multiplier"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Notes" id="extra-notes" name="notes" data-cy="notes" type="text" />
              <ValidatedField label="Capacity" id="extra-capacity" name="capacity" data-cy="capacity" type="select">
                {capacityValues.map(capacity => (
                  <option value={capacity} key={capacity}>
                    {capacity}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="extra-base" name="base" data-cy="base" label="Base" type="select">
                <option value="" key="0" />
                {baseExtras
                  ? baseExtras.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/extra" replace color="info">
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

export default ExtraUpdate;
