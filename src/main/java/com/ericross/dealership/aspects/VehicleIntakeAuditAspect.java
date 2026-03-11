package com.ericross.dealership.aspects;

import com.ericross.dealership.annotations.VehicleIntakeAudit;
import com.ericross.dealership.dtos.VehicleCandidateDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class VehicleIntakeAuditAspect {

    @Around("@annotation(vehicleIntakeAudit)")
    public Object around(ProceedingJoinPoint pjp, VehicleIntakeAudit vehicleIntakeAudit) throws Throwable {
        Object[] args = pjp.getArgs();

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof VehicleCandidateDto req) {
                if (req.getModel() != null) req.setModel(req.getModel().trim());
                if (req.getColor() != null) req.setColor(req.getColor().trim());

            }
        }
        return pjp.proceed(args);
    }
}
