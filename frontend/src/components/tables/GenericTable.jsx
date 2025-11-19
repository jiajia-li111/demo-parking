import { Table } from 'antd'


export default function GenericTable({ columns, data, loading, rowKey = 'id', pagination = true }) {
return (
<Table
rowKey={rowKey}
columns={columns}
dataSource={data}
loading={loading}
pagination={pagination}
/>
)
}