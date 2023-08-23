import React from 'react'
import { Button, Form } from 'react-bootstrap';
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom';

export default function RideRequestForm({ initialContents, submitAction, buttonLabel = "Create" }) {
    const navigate = useNavigate();
    
    // Stryker disable all
    const {
        register,
        formState: { errors },
        handleSubmit,
    } = useForm(
        { defaultValues: initialContents }
    );
    // Stryker enable all

    // Stryker disable next-line regex
    const timeRegex = /^(0?[1-9]|1[0-2]):[0-5][0-9] (AM|PM)$/;
    // Stryker enable all
   
    const testIdPrefix = "RideRequestForm";

    return (

        <Form onSubmit={handleSubmit(submitAction)}>

            {initialContents && (
                <Form.Group className="mb-3" >
                    <Form.Label htmlFor="id">Id</Form.Label>
                    <Form.Control
                        data-testid={testIdPrefix + "-id"}
                        id="id"
                        type="text"
                        {...register("id")}
                        defaultValue={initialContents.id}
                        disabled
                    />
                </Form.Group>
            )}

            

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="day">Day of Week</Form.Label>
                <Form.Select
                    data-testid={testIdPrefix + "-day"}
                    id="day"
                    isInvalid={Boolean(errors.day)}
                    {...register("day", {
                        required: "Day is required.",
                    })}
                >
                    <option value="">Select a Day</option>
                    <option value="Monday">Monday</option>
                    <option value="Tuesday">Tuesday</option>
                    <option value="Wednesday">Wednesday</option>
                    <option value="Thursday">Thursday</option>
                    <option value="Friday">Friday</option>
                    <option value="Saturday">Saturday</option>
                    <option value="Sunday">Sunday</option>
                </Form.Select>
                <Form.Control.Feedback type="invalid">
                    {errors.day?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="startTime">Start Time</Form.Label>
                <Form.Control
                    data-testid={testIdPrefix + "-startTime"}
                    id="startTime"
                    type="text"
                    isInvalid={Boolean(errors.startTime)}
                    {...register("startTime", {
                        required: "Start time is required.",
                        pattern: {
                            value: timeRegex,
                            message: "Please enter time in the format HH:MM AM/PM (e.g. 3:30 PM)."
                          }
                    })}
                    placeholder="Enter time in the format HH:MM AM/PM (e.g. 3:30 PM)"
                    defaultValue={initialContents?.startTime}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.startTime?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="endTime">End Time</Form.Label>
                <Form.Control
                    data-testid={testIdPrefix + "-endTime"}
                    id="endTime"
                    type="text"
                    isInvalid={Boolean(errors.endTime) }
                    {...register("endTime", {
                        required: "End time is required.",
                        pattern: {
                            value: timeRegex,
                            message: "Please enter time in the format HH:MM AM/PM (e.g. 3:30 PM)."
                        }
                    })}
                    placeholder="Enter time in the format HH:MM AM/PM (e.g. 3:30 PM)"   
                    defaultValue={initialContents?.endTime}     
                />
                <Form.Control.Feedback type="invalid">
                    {errors.endTime?.message}
                </Form.Control.Feedback>
            </Form.Group>
            
            <Form.Group className="mb-3" >
                <Form.Label htmlFor="pickupLocation">Pick-up Location</Form.Label>
                <Form.Control
                    data-testid={testIdPrefix + "-pickupLocation"}
                    id="pickupLocation"
                    type="text"
                    isInvalid={Boolean(errors.pickupLocation)}
                    {...register("pickupLocation", {
                        required: "Pick-up location is required."
                    })}
                    placeholder="e.g. Anacapa Residence Hall"  
                    defaultValue={initialContents?.pickupLocation} 
                />
                <Form.Control.Feedback type="invalid">
                    {errors.pickupLocation?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="pickupRoom">Pick-up Room #</Form.Label>
                <Form.Control
                    data-testid={testIdPrefix + "-pickupRoom"}
                    id="pickupRoom"
                    type="text"
                    isInvalid={Boolean(errors.pickupRoom)}
                    {...register("pickupRoom", {
                        required: "Pick-up room number is required."
                    })}
                    placeholder="e.g. 2225" 
                    defaultValue={initialContents?.pickupRoom} 
                />
                <Form.Control.Feedback type="invalid">
                    {errors.pickupRoom?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="dropoffLocation">Drop-off Location</Form.Label>
                <Form.Control
                    data-testid={testIdPrefix + "-dropoffLocation"}
                    id="dropoffLocation"
                    type="text"
                    isInvalid={Boolean(errors.dropoffLocation)}
                    {...register("dropoffLocation", {
                        required: "Drop-off location is required."
                    })}
                    placeholder="e.g. Phelps Hall"
                    defaultValue={initialContents?.dropoffLocation} 
                />
                <Form.Control.Feedback type="invalid">
                    {errors.dropoffLocation?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="dropoffRoom">Drop-off Room #</Form.Label>
                <Form.Control
                    data-testid={testIdPrefix + "-dropoffRoom"}
                    id="dropoffRoom"
                    type="text"
                    isInvalid={Boolean(errors.dropoffRoom)}
                    {...register("dropoffRoom", {
                        required: "Drop-off room number is required."
                    })}
                    placeholder="e.g. 2225" 
                    defaultValue={initialContents?.dropoffRoom} 
                />
                <Form.Control.Feedback type="invalid">
                    {errors.dropoffRoom?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="course">Course</Form.Label>
                <Form.Control
                    data-testid={testIdPrefix + "-course"}
                    id="course"
                    type="text"
                    isInvalid={Boolean(errors.course)}
                    {...register("course", {
                        required: "Course is required."
                    })}
                    placeholder="e.g. CMPSC156"  
                    defaultValue={initialContents?.course} 
                />
                <Form.Control.Feedback type="invalid">
                    {errors.course?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="notes">Notes</Form.Label>
                <Form.Control
                    data-testid={testIdPrefix + "-notes"}
                    id="notes"
                    type="text"
                    isInvalid={Boolean(errors.notes)}
                    {...register("notes", {
                        required: "Notes is required."
                    })}
                    placeholder="e.g. Bring your best smile :)"  
                    defaultValue={initialContents?.notes} 
                />
                <Form.Control.Feedback type="invalid">
                    {errors.notes?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Button
                type="submit"
                data-testid={testIdPrefix + "-submit"}
            >
                {buttonLabel}
            </Button>
            <Button
                variant="Secondary"
                onClick={() => navigate(-1)}
                data-testid={testIdPrefix + "-cancel"}
            >
                Cancel
            </Button>

        </Form>

    )
}