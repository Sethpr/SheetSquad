import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRefrence } from 'app/shared/model/refrence.model';
import { getEntity, updateEntity, createEntity, reset } from './refrence.reducer';

export const RefrenceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const refrenceEntity = useAppSelector(state => state.refrence.entity);
  const loading = useAppSelector(state => state.refrence.loading);
  const updating = useAppSelector(state => state.refrence.updating);
  const updateSuccess = useAppSelector(state => state.refrence.updateSuccess);

  const handleClose = () => {
    navigate('/refrence');
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
      ...refrenceEntity,
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
          ...refrenceEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheetSquadApp.refrence.home.createOrEditLabel" data-cy="RefrenceCreateUpdateHeading">
            Create or edit a Refrence
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="refrence-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Title" id="refrence-title" name="title" data-cy="title" type="text" />
              <ValidatedField label="Info" id="refrence-info" name="info" data-cy="info" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/refrence" replace color="info">
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

export default RefrenceUpdate;
