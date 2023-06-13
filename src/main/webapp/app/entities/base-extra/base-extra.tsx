import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBaseExtra } from 'app/shared/model/base-extra.model';
import { getEntities } from './base-extra.reducer';

export const BaseExtra = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const baseExtraList = useAppSelector(state => state.baseExtra.entities);
  const loading = useAppSelector(state => state.baseExtra.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="base-extra-heading" data-cy="BaseExtraHeading">
        Base Extras
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/base-extra/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Base Extra
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {baseExtraList && baseExtraList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Cost</th>
                <th>Refrence</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {baseExtraList.map((baseExtra, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/base-extra/${baseExtra.id}`} color="link" size="sm">
                      {baseExtra.id}
                    </Button>
                  </td>
                  <td>{baseExtra.name}</td>
                  <td>{baseExtra.cost}</td>
                  <td>{baseExtra.refrence ? <Link to={`/refrence/${baseExtra.refrence.id}`}>{baseExtra.refrence.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/base-extra/${baseExtra.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/base-extra/${baseExtra.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/base-extra/${baseExtra.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Base Extras found</div>
        )}
      </div>
    </div>
  );
};

export default BaseExtra;
