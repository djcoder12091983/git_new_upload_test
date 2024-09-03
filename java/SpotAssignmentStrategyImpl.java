package com.scaler.parking_lot.strategies.assignment;

import java.util.List;
import java.util.Optional;

import com.scaler.parking_lot.models.FloorStatus;
import com.scaler.parking_lot.models.ParkingFloor;
import com.scaler.parking_lot.models.ParkingLot;
import com.scaler.parking_lot.models.ParkingSpot;
import com.scaler.parking_lot.models.ParkingSpotStatus;
import com.scaler.parking_lot.models.VehicleType;

public class SpotAssignmentStrategyImpl implements SpotAssignmentStrategy{

    @Override
    public Optional<ParkingSpot> assignSpot(ParkingLot parkingLot, VehicleType vehicleType) {
        List<ParkingFloor> parkingFloors=parkingLot.getParkingFloors();
        List<ParkingFloor> sortedAvailableParkingFloors= parkingFloors.stream().filter(pf->pf.getStatus()==FloorStatus.OPERATIONAL).sorted((pf1,pf2)->{
            int pf1Spots=pf1.getSpots().size();
            int pf2Spots=pf2.getSpots().size();
            int pf1Number=pf1.getFloorNumber();
            int pf2Number=pf2.getFloorNumber();
            if(pf1Spots==pf2Spots){
                return pf1Number-pf2Number;
            }
            return pf1Spots-pf2Spots;
        }).toList();
        
        for(ParkingFloor parkingFloor:sortedAvailableParkingFloors){
            List<ParkingSpot> parkingSpots=parkingFloor.getSpots();
            List<ParkingSpot> sortedAvailableParkingSpots=parkingSpots.stream().filter(ps->ps.getSupportedVehicleType()==vehicleType && ps.getStatus()== ParkingSpotStatus.AVAILABLE).sorted((ps1,ps2)->{return ps1.getNumber()-ps2.getNumber();}).toList();
            if(sortedAvailableParkingFloors.size()>=1){
                return Optional.of(sortedAvailableParkingSpots.get(0));
            }
        }
        return Optional.empty();

    }
    
}