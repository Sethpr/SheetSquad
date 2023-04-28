import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IQuality } from 'app/shared/model/quality.model';
import { getEntities } from './quality.reducer';

export const Quality = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const qualityList = useAppSelector(state => state.quality.entities);
  const loading = useAppSelector(state => state.quality.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="quality-heading" data-cy="QualityHeading">
        Qualities
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/quality/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Quality
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {qualityList && qualityList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Capacity 1</th>
                <th>Capacity 2</th>
                <th>Capacity 3</th>
                <th>Cost</th>
                <th>Owner</th>
                <th>Extra</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {qualityList.map((quality, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/quality/${quality.id}`} color="link" size="sm">
                      {quality.id}
                    </Button>
                  </td>
                  <td>{quality.type}</td>
                  <td>{quality.capacity1}</td>
                  <td>{quality.capacity2}</td>
                  <td>{quality.capacity3}</td>
                  <td>{quality.cost}</td>
                  <td>{quality.owner ? <Link to={`/power/${quality.owner.id}`}>{quality.owner.id}</Link> : ''}</td>
                  <td>
                    {quality.extras
                      ? quality.extras.map((val, j) => (
                          <span key={j}>
                            <Link to={`/extra/${val.id}`}>{val.id}</Link>
                            {j === quality.extras.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/quality/${quality.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/quality/${quality.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/quality/${quality.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Qualities found</div>
        )}
      </div>
    </div>
  );
};

export default Quality;
