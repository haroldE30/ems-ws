package com.adeptsource.ems.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adeptsource.ems.common.util.CopyUtil;
import com.adeptsource.ems.dto.PositionDTO;
import com.adeptsource.ems.entity.Position;
import com.adeptsource.ems.exception.ResourceNotFoundException;
import com.adeptsource.ems.exception.TransactionProcessException;
import com.adeptsource.ems.repository.PositionRepository;
import com.adeptsource.ems.service.PositionService;

@Service
@Transactional
public class PositionServiceImpl implements PositionService {
	
	private static final Logger log = LoggerFactory.getLogger(PositionServiceImpl.class);
	
	private PositionRepository positionRepository;
	
	@Autowired
	public PositionServiceImpl(PositionRepository positionRepository) {
		this.positionRepository = positionRepository;
	}

	@Override
	public List<Position> getAll() throws ResourceNotFoundException {
		try {
			return positionRepository.findAll();
		} catch (Exception e) {
			log.error("Failed to load list of positions. {}", e.getMessage());
			throw new ResourceNotFoundException("Failed to load positions.", e);
		}
	}
	
	@Override
	public Position getById(Long id) throws ResourceNotFoundException {
		try {
		return positionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Position not found for id: " + id));
		} catch(Exception e) {
			log.error("Position not found for id: {}. {}" + id, e.getMessage());
			throw e;
		}
	}

	@Override
	public Position create(Position position) throws TransactionProcessException{
		try {
			return positionRepository.save(position);
		} catch (Exception e) {
			log.error("Failed to create position. {}", e.getMessage());
			throw new TransactionProcessException("Failed to create position. ", e);
		}
	}

	@Override
	public Position update(Long id, PositionDTO params) throws TransactionProcessException {
		try {
			Position position = getById(id);
			CopyUtil.copyNonNullProperties(params, position);
			return positionRepository.save(position);
		} catch(Exception e) {
			log.error("Failed to update position. {}", e.getMessage());
			throw new TransactionProcessException("Failed to update position with id: " + id, e);
		}
	}

	@Override
	public void delete(Long id) throws TransactionProcessException{
		try {
			Position position = getById(id);
			positionRepository.delete(position);
		} catch(Exception e) {
			log.error("Failed to delete position. {}", e.getMessage());
			throw new TransactionProcessException("Failed to delete position with id: " + id, e);
		}
	}

}
