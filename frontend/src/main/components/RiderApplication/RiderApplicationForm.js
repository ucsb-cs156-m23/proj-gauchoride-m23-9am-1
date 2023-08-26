import React from 'react'
import { Button, Form } from 'react-bootstrap';
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom';



function RiderApplicationForm({ initialContents, userEmail, submitAction, buttonLabel = "Create" }) {
    console.log(initialContents)
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
   
    const testIdPrefix = "RiderApplicationForm";


    return (

        <Form onSubmit={handleSubmit(submitAction)}>

            {initialContents && (
                <Form.Group className="mb-3" >
                    <Form.Label htmlFor="id">Applicant ID</Form.Label>
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
            {initialContents?.status && (
                <Form.Group className="mb-3" >
                    <Form.Label htmlFor="status">Status</Form.Label>
                    <Form.Control
                        data-testid={testIdPrefix + "-status"}
                        id="status"
                        type="text"
                        {...register("status")}
                        defaultValue={initialContents?.status}
                        disabled
                    />
                </Form.Group>
                
            )}
            {userEmail && (
                <Form.Group className="mb-3" >
                    <Form.Label htmlFor="email">Email</Form.Label>
                    <Form.Control
                        data-testid={testIdPrefix + "-email"}
                        id="email"
                        type="text"
                        {...register("email")}
                        defaultValue={userEmail}
                        disabled
                    />
                </Form.Group>
                
            )}
            <Form.Group className="mb-3" >
                <Form.Label htmlFor="perm_number">Perm Number</Form.Label>
                <Form.Control
                    data-testid={testIdPrefix + "-perm_number"}
                    id="perm_number"
                    type="text"
                    isInvalid={Boolean(errors.perm_number)}
                    {...register("perm_number", {
                        required: "Perm Number is required."
                    })}
                    defaultValue={initialContents?.perm_number} 
                />
                <Form.Control.Feedback type="invalid">
                    {errors.perm_number?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="description">Description</Form.Label>
                <Form.Control
                    as="textarea" 
                    rows="6"
                    data-testid={testIdPrefix + "-description"}
                    id="description"
                    type="text"
                    isInvalid={Boolean(errors.description)}
                    {...register("description", {
                        required: "Description is required."
                    })}
                    placeholder="Please describe the mobility limitations that cause you to need to use the Gauchoride service."
                    defaultValue={initialContents?.description} 
                />
                <Form.Control.Feedback type="invalid">
                    {errors.description?.message}
                </Form.Control.Feedback>
            </Form.Group>

            {initialContents?.notes && (
                <Form.Group className="mb-3" >
                    <Form.Label htmlFor="notes">Notes</Form.Label>
                    <Form.Control
                        data-testid={testIdPrefix + "-notes"}
                        id="notes"
                        type="text"
                        {...register("notes")}
                        defaultValue={initialContents?.notes}
                        disabled
                    />
                </Form.Group>
                
            )}
            {initialContents?.created_date && (
                <Form.Group className="mb-3" >
                    <Form.Label htmlFor="created_date">Date Applied:</Form.Label>
                    <Form.Control
                        data-testid={testIdPrefix + "-created_date"}
                        id="created_date"
                        type="text"
                        {...register("created_date")}
                        defaultValue={initialContents?.created_date}
                        disabled
                    />
                </Form.Group>
                
            )}
            {initialContents?.updated_date && (
                <Form.Group className="mb-3" >
                    <Form.Label htmlFor="updated_date">Last Updated:</Form.Label>
                    <Form.Control
                        data-testid={testIdPrefix + "-updated_date"}
                        id="updated_date"
                        type="text"
                        {...register("updated_date")}
                        defaultValue={initialContents?.updated_date}
                        disabled
                    />
                </Form.Group>
                
            )}
            {initialContents?.cancelled_date && (
                <Form.Group className="mb-3" >
                    <Form.Label htmlFor="cancelled_date">Application Cancelled On:</Form.Label>
                    <Form.Control
                        data-testid={testIdPrefix + "-cancelled_date"}
                        id="cancelled_date"
                        type="text"
                        {...register("cancelled_date")}
                        defaultValue={initialContents?.cancelled_date}
                        disabled
                    />
                </Form.Group>
                
            )}


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

export default RiderApplicationForm;