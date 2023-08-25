import React, { useState } from "react";
import { Row, Col , Modal, Button } from "react-bootstrap";
import RoleBadge from "main/components/Profile/RoleBadge";
import { useCurrentUser } from "main/utils/currentUser";
import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import axios from "axios";

const ProfilePage = () => {

    // Stryker disable all
    const [showModal, setShow] = useState(false);
    const [userCellPhone, setUserCellPhone] = useState("");
    // Stryker restore all

    const onSubmit = (event) => {
        axios.put(`/api/userprofile?cellPhone=${userCellPhone}`)
        event.preventDefault();
        window.location.reload();
    }
    

    const { data: currentUser } = useCurrentUser();

    if (!currentUser.loggedIn) {
        return (
            <p>Not logged in.</p>
        )
    }

    const { email, cellPhone, pictureUrl, fullName } = currentUser.root.user;
        
    // Stryker disable all
    
    const handleClose = async () => {
        await setShow(false);
        console.log(showModal+" modal")
    };
    const handleShow = async () => {
        await setUserCellPhone(cellPhone);
        await setShow(true);
        console.log(showModal+" modal")
    };
    // Stryker restore all
    return (
        <BasicLayout>
            <Row className="align-items-center profile-header mb-5 text-center text-md-left">
                <Col md={2}>
                    <img
                        src={pictureUrl}
                        alt="Profile"
                        className="rounded-circle img-fluid profile-picture mb-3 mb-md-0"
                    />
                </Col>
                <Col md>
                    <h2>{fullName}</h2>
                    <p className="lead text-muted">{email}</p>
                    <div>
                    <p className="lead text-muted" id="phone_number" data-testid="phone_number">{cellPhone}</p>
                    <Button variant="primary" onClick={handleShow} id="update_phone_btn">
                            Update Phone Number
                    </Button>
                    </div>
                    <RoleBadge role={"ROLE_USER"} currentUser={currentUser}/>
                    <RoleBadge role={"ROLE_MEMBER"} currentUser={currentUser}/>
                    <RoleBadge role={"ROLE_ADMIN"} currentUser={currentUser}/>
                    <RoleBadge role={"ROLE_DRIVER"} currentUser={currentUser}/>
                    <RoleBadge role={"ROLE_RIDER"} currentUser={currentUser}/>
                    {/* d-flex align-items-center justify-content-center */}
                    <div className="">

                        <Modal show={showModal} onHide={handleShow} data-testid="modal_id">
                            <Modal.Header closeButton onClick={handleClose}>
                                <Modal.Title>Update Phone Number:</Modal.Title>
                            </Modal.Header>
                            <Modal.Body>Enter New Phone Number</Modal.Body>
                            <form onSubmit={onSubmit}>
                                <input id="new_phone" data-testid="new_phone" placeholder="Enter New Phone Number" value={userCellPhone} name="new_phone" onChange={(e) => setUserCellPhone(e.target.value)}></input>
                            <Modal.Footer>
                                <Button type="submit">Update</Button>
                                <Button variant="secondary" id="cancel-button" onClick={handleClose}>Cancel</Button>
                            </Modal.Footer>
                            </form>
                        </Modal>
                    </div>


                </Col>
            </Row>
        </BasicLayout>
    );
};

export default ProfilePage;
