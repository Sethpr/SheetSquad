import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRefrence } from 'app/shared/model/refrence.model';
import { getEntities as getRefrences } from 'app/entities/refrence/refrence.reducer';
import { IBaseExtra } from 'app/shared/model/base-extra.model';
import { getEntity, updateEntity, createEntity, reset } from './base-extra.reducer';

export const BaseExtraUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const refrences = useAppSelector(state => state.refrence.entities);
  const baseExtraEntity = useAppSelector(state => state.baseExtra.entity);
  const loading = useAppSelector(state => state.baseExtra.loading);
  const updating = useAppSelector(state => state.baseExtra.updating);
  const updateSuccess = useAppSelector(state => state.baseExtra.updateSuccess);

  const handleClose = () => {
    navigate('/base-extra');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getRefrences({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...baseExtraEntity,
      ...values,
      refrence: refrences.find(it => it.id.toString() === values.refrence.toString()),
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
          ...baseExtraEntity,
          refrence: baseExtraEntity?.refrence?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheetSquadApp.baseExtra.home.createOrEditLabel" data-cy="BaseExtraCreateUpdateHeading">
            Create or edit a Base Extra
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="base-extra-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="base-extra-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Value" id="base-extra-value" name="value" data-cy="value" type="text" />
              <ValidatedField id="base-extra-refrence" name="refrence" data-cy="refrence" label="Refrence" type="select">
                <option value="" key="0" />
                {refrences
                  ? refrences.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/base-extra" replace color="info">
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

export default BaseExtraUpdate;
