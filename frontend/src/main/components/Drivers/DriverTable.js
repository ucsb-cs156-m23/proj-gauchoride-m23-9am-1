
import OurTable from "main/components/OurTable"



export default function DriverTable({ users}) {

    const columns = [
        {
            Header: 'First Name',
            accessor: 'givenName',
        },
        {
            Header: 'Last Name',
            accessor: 'familyName',
        },
        {
            Header: 'Phone Number',
            accessor: 'cellPhone',
        },
        {
            Header: 'Email',
            accessor: 'email'
        }
    ];

    return <OurTable
        data={users}
        columns={columns}
        testid={"DriverTable"}
    />;
};