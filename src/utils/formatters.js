import dayjs from 'dayjs'


export const fmtDateTime = (v) => (v ? dayjs(v).format('YYYY-MM-DD HH:mm:ss') : '-')
export const fmtMoney = (v) => (v == null ? '-' : Number(v).toFixed(2))