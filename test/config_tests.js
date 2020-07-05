const ASSERT = require('assert'),
    FILE_SYSTEM = require('fs'),
    YAML = require('js-yaml');

const FILE_CONTENTS = FILE_SYSTEM.readFileSync('./config.yaml');
const CONFIG = YAML.safeLoadAll(FILE_CONTENTS)[0];

describe('config file', () => {
    it('is not empty', () => {
        ASSERT.equal(CONFIG === undefined, false);
    });
    (CONFIG === undefined ? xdescribe : describe)('properties', () => {
        it(`token property exists`, () => {
            ASSERT.equal('token' in CONFIG, true);
        });
    });
});