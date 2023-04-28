import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPower } from 'app/shared/model/power.model';
import { getEntities as getPowers } from 'app/entities/power/power.reducer';
import { IExtra } from 'app/shared/model/extra.model';
import { getEntities as getExtras } from 'app/entities/extra/extra.reducer';
import { IQuality } from 'app/shared/model/quality.model';
import { QualityType } from 'app/shared/model/enumerations/quality-type.model';
import { Capacity } from 'app/shared/model/enumerations/capacity.model';
import { getEntity, updateEntity, createEntity, reset } from './quality.reducer';

export const QualityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const powers = useAppSelector(state => state.power.entities);
  const extras = useAppSelector(state => state.extra.entities);
  const qualityEntity = useAppSelector(state => state.quality.entity);
  const loading = useAppSelector(state => state.quality.loading);
  const updating = useAppSelector(state => state.quality.updating);
  const updateSuccess = useAppSelector(state => state.quality.updateSuccess);
  const qualityTypeValues = Object.keys(QualityType);
  const capacityValues = Object.keys(Capacity);

  const handleClose = () => {
    navigate('/quality');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPowers({}));
    dispatch(getExtras({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...qualityEntity,
      ...values,
      extras: mapIdList(values.extras),
      owner: powers.find(it => it.id.toString() === values.owner.toString()),
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
          type: 'ATTACK',
          capacity1: 'SPEED',
          capacity2: 'SPEED',
          capacity3: 'SPEED',
          ...qualityEntity,
          owner: qualityEntity?.owner?.id,
          extras: qualityEntity?.extras?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheetSquadApp.quality.home.createOrEditLabel" data-cy="QualityCreateUpdateHeading">
            Create or edit a Quality
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="quality-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Type" id="quality-type" name="type" data-cy="type" type="select">
                {qualityTypeValues.map(qualityType => (
                  <option value={qualityType} key={qualityType}>
                    {qualityType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Capacity 1" id="quality-capacity1" name="capacity1" data-cy="capacity1" type="select">
                {capacityValues.map(capacity => (
                  <option value={capacity} key={capacity}>
                    {capacity}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Capacity 2" id="quality-capacity2" name="capacity2" data-cy="capacity2" type="select">
                {capacityValues.map(capacity => (
                  <option value={capacity} key={capacity}>
                    {capacity}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Capacity 3" id="quality-capacity3" name="capacity3" data-cy="capacity3" type="select">
                {capacityValues.map(capacity => (
                  <option value={capacity} key={capacity}>
                    {capacity}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Cost"
                id="quality-cost"
                name="cost"
                data-cy="cost"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField id="quality-owner" name="owner" data-cy="owner" label="Owner" type="select">
                <option value="" key="0" />
                {powers
                  ? powers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Extra" id="quality-extra" data-cy="extra" type="select" multiple name="extras">
                <option value="" key="0" />
                {extras
                  ? extras.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/quality" replace color="info">
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

export default QualityUpdate;
